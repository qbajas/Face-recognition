package data;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 *
 * @author Michal
 */
public class PCADataProcessor extends DataProcessor {

    @Override
    public double[][] processData(double[][] data) {
       
        throw new RuntimeException("Not supportet yet");
    }

    @Override
    public double[] getProjection(double[] data) {
        
        
        throw new RuntimeException("Not supportet yet");
    }
    
    
}

/**
 * Klasa wykonujaca analize skladowych glownych (PCA) na wejsciowej macierzy
 * @author Michal
 */
class PCA{
    
    private DenseDoubleMatrix2D components;
    private int componentsCount;
    
    private DenseDoubleMatrix2D data = new DenseDoubleMatrix2D(1,1);
    
    private int sampleIndex;
    private double mean[];

    /**
     * Utworzenie obiektu dla danych wejsciowych, przyjmowana liczba skladowych
     * glownych okreslana jest na liczbe wejsciowych wierszy (liczba obrazow)
     * @param input 
     */
    public PCA(double[][] input){
        this(input,input.length);
    }
        
    /**
     * Utworzenie obiektu dla danych wejsciowych oraz dla okreslonej liczby
     * skladowych glownych
     * @param input
     * @param components 
     */
    public PCA(double[][] input, int components) {
        this.data = new DenseDoubleMatrix2D(input);
        componentsCount = -1;
        mean = new double[input[0].length];
        
        int colLen = input[0].length;
        
        for(int row = 0; row<input.length;++row)
            for(int col = 0; col<colLen;++col)
                mean[col]+= input[row][col];
        
        for(int i=0;i<colLen;++i)
            mean[i]/= input.length;
        
        for(int row = 0; row<input.length;++row)
            for(int col = 0; col<colLen;++col)
                data.set(row, col, data.getQuick(row, col) - mean[col]); //TODO change to setQuick/getQuick
    }


    
}

