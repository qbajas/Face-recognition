package ann;

import Utils.Config;
import Utils.Logger;
import data.DataLoader;
import data.DataProcessor;
import data.ImageProcessor;
import data.ImageTrainingSetLoader;
import java.io.*;
import views.ConsoleOutput;

/**
 *
 * @author Michal
 */
public class ANNManager {

    Logger logger = new Logger();

    /**
     * Tworzy nowa siec w oparciu o podane procesory danych, sam ustala rozmiar
     * wejsc, wyjsc, neuronow ukrytych.
     *
     * @param imgProcessor Procesor obrazu, jesli null to konwersja na obraz w
     * odcieni szarosci i zmiana na double[]
     * @param dataProcessor Procesor przetwarzajacy wstepnie dane, domyslnie PCA
     * o rozmiarze 200
     * @param forceNew Tworzy nowa siec i ewentualnie przelicza PCA nawet jesli
     * siec jest zapisana i mozna ja odczytac
     * @return Wczytana lub utworzona siec neuronowa
     */
    public ANN getANN(ImageProcessor imgProcessor, DataProcessor dataProcessor, boolean forceNew) {
        
        DataLoader loader = new ImageTrainingSetLoader(imgProcessor, dataProcessor);
        File file = new File(Config.dataPath + File.separatorChar + "ANN" + imgProcessor.getName() + dataProcessor.getName() + ".ann");
        ANN ann = null;
        if (file.exists()) {
            logger.log("Loading ANN...");
            ann=loadANN(file);
            ann.setLogger(logger);
            ann.setLoader(loader);
            logger.log("Done.");
        }
            
        if(ann==null){
            logger.log("Creating new ANN...");
            ann = new ANN(loader);
        }
        
        logger.log("Done.");
        return ann;
    }

    private ANN loadANN(File file) {
        ANN ann;
        try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            logger.log("Loading ANN Object...");
            ann = (ANN) in.readObject();
            return ann;
        } catch (ClassNotFoundException | IOException e) {
            logger.log("Can't load...");
        }
        return null;
    }

    public void saveANN(ANN ann) {
        File file = new File(Config.dataPath + File.separatorChar + "ANN" + ann.getImageProcessor().getName() + ann.getProcessor().getName() + ".ann");
        try (FileOutputStream fileOut = new FileOutputStream(file); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            logger.log("Saving ANN...");
            out.writeObject(ann);
        } catch (IOException e) {
            logger.log("Can't save ANN");
        }
    }

    public void removeConsoleOutputs() {
        logger.removeConsoleOutputs();
    }

    public void removeConsoleOutput(ConsoleOutput output) {
        logger.removeConsoleOutput(output);
    }

    public void addConsoleOutput(ConsoleOutput output) {
        logger.addConsoleOutput(output);
    }
}
