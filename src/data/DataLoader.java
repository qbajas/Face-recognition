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

public interface DataLoader {
    
    void setDataProcessor(DataProcessor processor);
    DataProcessor getPreprocessor();
    MLData loadData(String source);
    
}
