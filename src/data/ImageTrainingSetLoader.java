/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import org.encog.ml.data.MLData;

/**
 *
 * @author Michal
 */
public class ImageTrainingSetLoader implements DataLoader{

    private ImageProcessor imgProcessor;
    private DataProcessor dataProcessor;
    
    @Override
    public void setDataProcessor(DataProcessor processor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataProcessor getPreprocessor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MLData loadData(String source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ImageProcessor getImgProcessor() {
        return imgProcessor;
    }

    public void setImgProcessor(ImageProcessor imgProcessor) {
        this.imgProcessor = imgProcessor;
    }
    
    
    
}
