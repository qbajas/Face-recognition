package data;

import org.encog.ml.data.MLDataSet;

/**
 * Podstawowy interfejs dla klas wczytujacych obrazy testowe
 *
 * @author Michal
 */
public interface DataLoader {

    void setDataProcessor(DataProcessor processor);

    void setImageProcessor(ImageProcessor processor);

    DataProcessor getDataProcessor();

    ImageProcessor getImageProcessor();

    void loadData(String source);
    
    MLDataSet getTrainingSet();
    
    MLDataSet getGeneralizationSet();
    
    MLDataSet getTestSet();
    
    public int getInputLength(String path);
    
    public boolean isLoaded();
    
}
