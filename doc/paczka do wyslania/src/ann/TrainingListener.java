package ann;

/**
 * Interfejs do sledzenia zmian dokladnosci sieci
 * @author Michal
 */
public interface TrainingListener {
    
    /**
     * Siec rozpoczela trening, inicjalizuj
     */
    public void trainingStarted();
    /**
     * Siec zakonczyla trening, clean-up
     */
    public void trainingFinished();
    /**
     * Nastapila kolejna iteracja treningu
     * @param iteration Numer iteracji, 1-n
     * @param errorRate Blad sredniokwadratowy
     * @param trainingAccuracy Dokladnosc osiagnieta na zbiorze treningowym <0,1>
     * @param classificationAccuracy Dokladnosc klasyfikacji (poprawne rozpoznanie twarzy) <0,1>
     * @param falsePositiveCount Liczba bledow klasyfikacji, gdy na wejsciu otrzymano twarz,
     * ktora nie znajduje sie w bazie i nie powinna byc przypisana zadnej osobie, a siec
     * sklasyfikowala ja jako twarz bedaca w bazie.
     */
    public void trainingUpdate(double errorRate, double trainingAccuracy, double generalizationAccuracy);
    
    
}
