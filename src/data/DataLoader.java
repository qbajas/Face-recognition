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

    MLDataSet loadData(String source);
}
