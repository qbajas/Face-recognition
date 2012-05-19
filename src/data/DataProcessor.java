package data;

/**
 * Klasa przetwarzajaca wstepnie dane wejsciowe dla sieci.
 * @author Michal
 */
public class DataProcessor {
    
    /**
     * Funkcja zwraca przetworzone dane wejsciowe dla sieci. Podstawowa implementacja
     * nie wykonuje zadnych operacji na wejsciu. Umozliwi to testowanie sieci
     * bez stosowania PCA oraz LDA.
     * @param data Dane wejsciowe
     * @return Przetworzone dane wejsciowe dla sieci
     */
    public double[] processData(double[] data){
        return data;
    }
}
