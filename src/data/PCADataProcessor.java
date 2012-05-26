package data;

import Utils.Config;
import data.pca.PCA;
import data.pca.PCAManager;

/**
 *
 * @author Michal
 */
public class PCADataProcessor extends DataProcessor {

    private PCA pcaHelper;
    private int length;

    public PCADataProcessor(int length) {
        this.length = length;
    }
    
    @Override
    public double[][] processData(double[][] data) {
        
        PCAManager pcaLoader = new PCAManager(Config.dataPath);
        if(pcaHelper==null || pcaHelper.getComponentsCount()==length){
            pcaHelper=null;
            pcaHelper = pcaLoader.getPCA(data,length, false);
        }
        
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

    @Override
    public String getName() {
        return "PCA" + length;
    }
    
    
    
}
