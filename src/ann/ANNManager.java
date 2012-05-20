package ann;

import data.DataProcessor;
import data.ImageProcessor;
import java.awt.image.BufferedImage;

/**
 *
 * @author Michal
 */
public class ANNManager {
    
    private DataProcessor processor;
    private ImageProcessor imageProcessor;
    boolean trained = false;
    
    public void train(TrainMethod method){
        throw new RuntimeException("Not supported");
    }

    public void getANN(int inputs, int hidden, int outputs){
        throw new RuntimeException("Not supported");
    }
    
    public int getSubjectNbr(BufferedImage img){
        
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
    }
    
    
    public enum TrainMethod {
        BackPropagation, ResilentPropagation
    }
}
