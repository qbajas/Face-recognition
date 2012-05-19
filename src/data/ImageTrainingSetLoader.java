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
    public MLData loadData(String source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setDataProcessor(DataProcessor processor) {
        dataProcessor = processor;
    }
    
    @Override
    public DataProcessor getDataProcessor() {
        return dataProcessor;
    }
    
    @Override
    public void setImageProcessor(ImageProcessor processor) {
        imgProcessor = processor;
    }

    @Override
    public ImageProcessor getImageProcessor() {
        return imgProcessor;
    }
}
