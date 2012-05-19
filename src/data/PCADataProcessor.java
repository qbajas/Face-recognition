package data;

import data.pca.PCA;
import data.pca.PCAManager;

/**
 *
 * @author Michal
 */
public class PCADataProcessor extends DataProcessor {

    private PCA pcaHelper;
    
    @Override
    public double[][] processData(double[][] data) {
        PCAManager pcaLoader = new PCAManager("dataSet");
        pcaHelper = pcaLoader.getPCA(data,100, false);
        double [][] result = new double[data.length][];
        for(int row = 0;row<result.length;++row)
            result[row] = pcaHelper.projection(data[row]);
        System.out.println("COLUMNS = "+ result[0].length);
        return result;
    }

    @Override
    public double[] getProjection(double[] data) {
        if(pcaHelper == null)
            throw new RuntimeException("Process data first!");

        return pcaHelper.projection(data);
    }
}
