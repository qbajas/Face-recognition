package ann;

import Utils.Config;
import Utils.Logger;
import data.DataLoader;
import data.DataProcessor;
import data.ImageProcessor;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 *
 * @author Michal
 */
public class ANN implements Serializable{
    private static final long serialVersionUID = 12L;


    private double momentum = 0.3;
    private double learnRate = 0.7;
    private double errorRate = 0.01;
    private int maxIt = 1000;
    private double minAccuracy = 1;
    
    private double threshold = 0.9;
    
    boolean trained = false;
    private BasicNetwork network;
    private transient DataLoader loader;
    private transient DataProcessor processor;
    private transient ImageProcessor imageProcessor;
    private transient Logger logger; 
    private transient List<TrainingListener> listeners;
    
    ANN(DataLoader loader) {
        this.processor = loader.getDataProcessor();
        this.imageProcessor = loader.getImageProcessor();
        this.loader = loader;
        logger = new Logger();
    }

    public void train(TrainMethod method, boolean forceTraining) {
        if (trained && !forceTraining) {
            logger.log("ANN is trained and ready to use");
            return;
        }
          
        final Propagation train;
        final MLDataSet trainSet = loader.getTrainingSet();
        int input = trainSet.getInputSize();
        int output = trainSet.getIdealSize();
        int hidden = 300;
        
        if(network==null)
            getANN(input,hidden,output);
        
        new ConsistentRandomizer(-1, 1, 500).randomize(network);
        
        switch (method) {
            case ResilentPropagation: {
                logger.log("Training using ResilentPropagation");
                train = new ResilientPropagation(network, trainSet);
                break;
            }
            default:
            case BackPropagation: {
                logger.log("Training using BackPropagation");
                train = new Backpropagation(network, trainSet, learnRate, momentum);
                break;
            }
        }

        
        new Thread(new Runnable() {

            @Override
            public void run() {
                int epoch = 1;
                
                double trainAcc = 0;
                double error = 0;
                double classAcc = 0;
                int falseCount = 0;
                
                MLDataSet generalizationSet = loader.getGeneralizationSet();
                MLDataSet testSet = loader.getTestSet();
                MLDataSet falseSet = loader.getFalseSet();
                
                notifyStarted();
                do {
                    train.iteration();
                    error = train.getError();
                    trainAcc = getAccuracy(trainSet);
                    
                    logger.log("Epoch " + epoch + " Error: " + train.getError());
                    logger.log("Accuracy = "+getAccuracy(trainSet));
                    epoch++;
                    
                } while (getAccuracy(trainSet) < minAccuracy  && epoch<maxIt);

                trained = true;
                notifyFinished();
            }
        }).start();

    }


    

    private void getANN(int inputs, int hidden, int outputs) {

        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, inputs));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hidden));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs));
        network.getStructure().finalizeStructure();
        network.reset();
        trained = false;
    }

    public int getSubjectNbr(BufferedImage img) {
        double[] input = processor.getProjection(imageProcessor.process(img));
        return network.classify(new BasicMLData(input));
    }

    public void setImageProcessor(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    public DataProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(DataProcessor processor) {
        this.processor = processor;
    }

    public ImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    public enum TrainMethod {

        BackPropagation, ResilentPropagation
    }

    public double getErrorRate() {
        return errorRate;
    }
    
    public double getAccuracy(MLDataSet set){
        Iterator<MLDataPair> it = set.iterator();
        MLDataPair pair;
        double[] output;
        int counter = 0;
        while(it.hasNext()){
            pair = it.next();
            output = new double[pair.getIdealArray().length];
            network.compute(pair.getInputArray(), output);
            if(compare(pair.getIdealArray(),output,threshold))
                counter++;    
        }
        
        return (double)counter/set.getRecordCount();
    }
    
    
    private boolean compare(double[] ideal,double[] output, double thresh){
        if(ideal.length != output.length)
            return false;
        int i;
        int maxInd = 0;
        double max = output[0];
        for(i=1;i<output.length;++i)
            if(max<output[i]){
                max = output[i];
                maxInd = i;
            }
        if(max<thresh || ideal[maxInd]!=1)
            return false;
        return true;
        
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public double getLearnRate() {
        return learnRate;
    }

    public void setLearnRate(double learnRate) {
        this.learnRate = learnRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setLoader(DataLoader loader) {
        this.loader = loader;
        imageProcessor = loader.getImageProcessor();
        processor = loader.getDataProcessor();
    }

    public int getMaxIt() {
        return maxIt;
    }

    public void setMaxIt(int maxIt) {
        this.maxIt = maxIt;
    }

    public double getMinAccuracy() {
        return minAccuracy;
    }

    public void setMinAccuracy(double minAccuracy) {
        this.minAccuracy = minAccuracy;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void addTrainingListener(TrainingListener listener){
        listeners.add(listener);
    }
 
    public void removeTrainingListeners(){
        listeners.clear();
    }

    public List<TrainingListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<TrainingListener> listeners) {
        this.listeners = listeners;
    }
    
    private void notifyStarted(){
        for(TrainingListener listener:listeners)
            listener.trainingStarted();
    }
    
    private void notifyFinished(){
        for(TrainingListener listener:listeners)
            listener.trainingFinished();
    }
    

    
}
