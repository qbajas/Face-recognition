package data;
import org.encog.ml.data.MLData;
/**
 * Podstawowy interfejs dla klas wczytujacych obrazy testowe
 * @author Michal
 */

public interface DataLoader {
    
    void setDataProcessor(DataProcessor processor);
    void setImageProcessor(ImageProcessor processor);
    DataProcessor getDataProcessor();
    ImageProcessor getImageProcessor();
    MLData loadData(String source);
    
}
