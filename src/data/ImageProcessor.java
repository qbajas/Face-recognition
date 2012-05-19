package data;

/**
 * Interfejs dla klas przetwarzajacych obraz wejsciowy na dane numeryczne
 * @author Michal
 */
public interface ImageProcessor {
    /**
     * Zwraca liczbowe wartosci pikseli
     * @param img Obraz wejsciowy
     * @return Wartosci pikseli po przetworzeniu obrazu
     */
    double[] process(java.awt.image.BufferedImage img);
}
