package ann;

import Utils.Config;
import Utils.Logger;
import data.DataLoader;
import data.DataProcessor;
import data.ImageProcessor;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
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

import controllers.NetworkStatsController;

/**
 *
 * @author Michal
 */
public class ANN implements Serializable {

    private static final long serialVersionUID = 12L;
    private double momentum = 0.3;
    private double learnRate = 0.7;
    private double errorRate = 0.01;
    private int maxIt = 110;
    private double minAccuracy = 1;
    private double threshold = 0.95;
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
        int hidden = 500;

        if (network == null) {
            getANN(input, hidden, output);
        }

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


        int epoch = 1;

        double trainAcc;
        double error;
        double genAcc;

        MLDataSet generalizationSet = loader.getGeneralizationSet();


        notifyStarted();
        do {
            train.iteration();
            error = train.getError();
            trainAcc = getAccuracy(trainSet);
            genAcc = getAccuracy(generalizationSet);

            logger.log("Epoch " + epoch + " Error: " + error);
            logger.log("Training set accuracy = " + trainAcc);
            logger.log("Generalization set accuracy" + genAcc);

            epoch++;

            notifyUpdated(error, trainAcc, genAcc);

        } while ((getAccuracy(trainSet) < minAccuracy || getAccuracy(generalizationSet) < minAccuracy) && epoch < maxIt);

        trained = true;
        notifyFinished();
        logger.log("Finished");



    }

    private void getANN(int inputs, int hidden, int outputs) {

        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, inputs));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hidden));
       // network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hidden));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs));
        network.getStructure().finalizeStructure();
        network.reset();
        trained = false;
    }

    public int getSubjectNbr(BufferedImage img) {
        double[] input = processor.getProjection(imageProcessor.process(img));
        if(input==null){
            loader.loadData(Config.dataPath, Config.falseDataPath);
            input = processor.getProjection(imageProcessor.process(img));
        }
        double[] output = new double[network.getOutputCount()];
        network.compute(input, output);
        return getSubject2(output);
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
    
    private int getSubject2(double[] output) {

        int it = 0;
        double max = output[0];

        for (int i = 1; i < output.length; ++i) {
            if (output[i] > max) {
                it = i;
                max = output[i];
            }
        }
        return max >= threshold ? it+1 : 0;
    }

    private int getSubject(double[] output) {

        int it = 0;
        double max = output[0];

        for (int i = 1; i < output.length; ++i) {
            if (output[i] > max) {
                it = i;
                max = output[i];
            }
        }
        return max >= threshold ? it : 0;
    }

    public double getAccuracy(MLDataSet set) {
        Iterator<MLDataPair> it = set.iterator();
        MLDataPair pair;
        double[] output;
        int counter = 0;
        while (it.hasNext()) {
            pair = it.next();
            output = new double[pair.getIdealArray().length];
            network.compute(pair.getInputArray(), output);
            //if(compare(pair.getIdealArray(),output,threshold))
            double [] ideal = pair.getIdealArray();
           
            if (getSubject(pair.getIdealArray()) == getSubject(output)) {
                counter++;
            }

        }

        return (double) counter / set.getRecordCount();
    }

    private boolean compare(double[] ideal, double[] output) {
        if (ideal.length != output.length) {
            return false;
        }
        int i;
        int maxInd = 0;
        double max = output[0];
        for (i = 1; i < output.length; ++i) {
            if (max < output[i]) {
                max = output[i];
                maxInd = i;
            }
        }
        if (max < threshold || ideal[maxInd] != 1) {
            return false;
        }
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

    public void addTrainingListener(TrainingListener listener) {
        listeners.add(listener);
    }

    public void removeTrainingListeners() {
        listeners.clear();
    }

    public List<TrainingListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<TrainingListener> listeners) {
        this.listeners = listeners;
    }

    private void notifyStarted() {
        for (TrainingListener listener : listeners) {
            listener.trainingStarted();
        }
    }

    private void notifyFinished() {
        for (TrainingListener listener : listeners) {
            listener.trainingFinished();
        }
    }

    private void notifyUpdated(double errorRate, double trainingAccuracy, double generalizationAccuracy) {
        for (TrainingListener listener : listeners) {
            listener.trainingUpdate(errorRate, trainingAccuracy, generalizationAccuracy);
        }
    }

    public boolean isTrained() {
		return trained;
	}
    
}
