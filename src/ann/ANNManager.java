package ann;

import data.DataProcessor;
import data.ImageProcessor;
import data.ImageTrainingSetLoader;
import java.awt.image.BufferedImage;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 *
 * @author Michal
 */
public class ANNManager {

    private double momentum = 0.3;
    private double learnRate = 0.7;
    private double errorRate = 0.01;
    private DataProcessor processor;
    private ImageProcessor imageProcessor;
    boolean trained = false;
    private BasicNetwork network;
    private ImageTrainingSetLoader loader;

    public void train(TrainMethod method) {
        Propagation train;
        MLDataSet trainSet = loader.getTrainingSet();
        new ConsistentRandomizer(-1,1,500).randomize(network);
        switch (method) {
            case ResilentPropagation: {
                train = new ResilientPropagation(network, trainSet);
                break;
            }
            default:
            case BackPropagation: {
                train = new Backpropagation(network, trainSet, learnRate, momentum);
                break;
            }
        }
        int epoch = 1;

        do {
            train.iteration();
            System.out.println("Epoch " + epoch + " Error:" + train.getError());
            epoch++;
        } while (train.getError() > errorRate);
        
        trained = true;
    }

    public void getANN(int inputs, int hidden, int outputs,boolean forceNew) {

        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, inputs));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hidden));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs));
        network.getStructure().finalizeStructure();
        network.reset();
        trained = false;
    }

    public int getSubjectNbr(BufferedImage img) {

        throw new RuntimeException("Not supported");
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

    public ANNManager(DataProcessor processor, ImageProcessor imageProcessor) {
        this.processor = processor;
        this.imageProcessor = imageProcessor;
        loader = new ImageTrainingSetLoader(imageProcessor, processor);
    }

    public enum TrainMethod {

        BackPropagation, ResilentPropagation
    }

    public double getErrorRate() {
        return errorRate;
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
    
    
}
