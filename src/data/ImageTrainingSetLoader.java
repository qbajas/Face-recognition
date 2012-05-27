package data;

import Utils.Config;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;

/**
 * Klasa do wczytywania wszystkich zdjec twarzy z katalogu bazy obrazow,
 * pogrupowanych w foldery odpowiadajace poszczegolnym osobom
 *
 * @author Michal
 */
public class ImageTrainingSetLoader implements DataLoader {

    private MLDataSet trainingSet;
    private MLDataSet testSet;
    private MLDataSet generalizationSet;
    private MLDataSet falseSet;
    private boolean loaded = false;
    private static String[] imagesPath = {
        "_0.jpg", "_+05.jpg","_+10.jpg","_+15.jpg", "_+25.jpg", "_+45.jpg","_+65.jpg", "_+75.jpg","_+90.jpg",
        "_-05.jpg","_-10.jpg","_-15.jpg", "_-25.jpg", "_-45.jpg","_-65.jpg", "_-75.jpg","_-90.jpg", "_+5.jpg", "_-5.jpg"
    };
    private ImageProcessor imgProcessor;
    private DataProcessor dataProcessor;

    /**
     * Umożliwia ustawienie dodatkowych
     *
     * @param imgProcessor
     * @param dataProcessor
     */
    public ImageTrainingSetLoader(ImageProcessor imgProcessor, DataProcessor dataProcessor) {
        this.imgProcessor = imgProcessor;
        this.dataProcessor = dataProcessor;
    }

    /**
     * Domyslny konstruktor, konwersja obrazu do odcieni szarosci, brak dalszej
     * konwersji danych
     */
    public ImageTrainingSetLoader() {
        imgProcessor = new ImageToVectorProcessor(true);
        dataProcessor = new DataProcessor();
    }

    public void loadData(String source, String falseSource) {
        File mainFolder = new File(source);
        File subFolders[] = mainFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        FileFilter jpgFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".jpg");
            }
        };

        FileFilter testFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                for (String s : imagesPath) {
                    if (name.toLowerCase().startsWith("a") && name.toLowerCase().endsWith(s.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        };

        File images[];
        int subjectNbr;
        int subjectsCount = subFolders.length;
        OutputContainer container = new OutputContainer(subjectsCount);

        LinkedList<MLDataPair> trainPairs = new LinkedList<>();
        LinkedList<MLDataPair> pairs = new LinkedList<>();
        
        System.out.println("Loading dataSet ...");
        for (File folder : subFolders) {
            subjectNbr = Integer.parseInt(folder.getName().replace("Subject", ""));
            System.out.println(folder.getName());
            images = folder.listFiles(jpgFilter);

            for (File image : images) {
                try {
                    if (testFilter.accept(image)) {
                        trainPairs.add(getPair(imgProcessor.process(ImageIO.read(image)), container.getIdealOutput(subjectNbr)));
                    } else {
                        pairs.add(getPair(imgProcessor.process(ImageIO.read(image)), container.getIdealOutput(subjectNbr)));
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        subFolders = null;
        double[][] annInputs = new double[trainPairs.size()][];
        double[][] annOutputs = new double[trainPairs.size()][];

        int counter = 0;
        for (MLDataPair pair : trainPairs) {
            annInputs[counter] = pair.getInputArray();
            annOutputs[counter] = pair.getIdealArray();
            ++counter;
        }
        annInputs = dataProcessor.processData(annInputs);
        trainingSet = new BasicMLDataSet(annInputs, annOutputs);
        
        annInputs= null;
        annOutputs = null;
        
        System.out.println("Training set size = " +trainingSet.size());
        Collections.shuffle(pairs);
        counter = (int) (pairs.size() *0.3); //30% pozostalych danych zbior generalizacyjny
        
        generalizationSet = new BasicMLDataSet();
        testSet = new BasicMLDataSet();
        falseSet = new BasicMLDataSet();
        
        int i=-1;
        for(MLDataPair pair:pairs){
            if(++i<counter)
                generalizationSet.add(getPair(dataProcessor.getProjection(pair.getInputArray()), pair.getIdealArray()));
            else
                testSet.add(getPair(dataProcessor.getProjection(pair.getInputArray()), pair.getIdealArray()));
        }
        pairs = null;
        System.out.println("Generalization set size = " +generalizationSet.size());
        System.out.println("Test set size = " +testSet.size());
        
        mainFolder = new File(falseSource);
        subFolders = mainFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        
        for (File folder : subFolders) {
            System.out.println(folder.getName());
            images = folder.listFiles(jpgFilter);
            for (File image : images) {
                try {
                        falseSet.add(getPair(dataProcessor.getProjection(imgProcessor.process(ImageIO.read(image))), container.getIdealOutput(0)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        System.out.println("False positive test set size = "+falseSet.size());
        loaded = true;
    }

    private MLDataPair getPair(double[] input, double[] output) {
        return new BasicMLDataPair(new BasicMLData(input), new BasicMLData(output));
    }
    


    @Override
    public MLDataSet getTrainingSet() {
        if (trainingSet == null) {
            loadData(Config.dataPath, Config.falseDataPath);
        }
        return trainingSet;
    }

    @Override
    public MLDataSet getGeneralizationSet() {
        if (generalizationSet == null) {
            loadData(Config.dataPath, Config.falseDataPath);
        }
        return generalizationSet;
    }

    @Override
    public MLDataSet getTestSet() {
        if (testSet == null) {
            loadData(Config.dataPath, Config.falseDataPath);
        }
        return testSet;
    }

    @Override
    public MLDataSet getFalseSet() {
        if(falseSet ==null){
            loadData(Config.dataPath, Config.falseDataPath);
        }
        return falseSet;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Zwraca dlugosc pojedynczej probki
     *
     * @return
     */
    @Override
    public int getInputLength(String path) {
        try {
            return dataProcessor.getProjection(imgProcessor.process(ImageIO.read(new File(path)))).length;
        } catch (IOException ex) {
        }
        return -1;
    }

    @Override
    public void setDataProcessor(DataProcessor processor) {
        dataProcessor = processor;
    }

    @Override
    public DataProcessor getDataProcessor() {
        return dataProcessor;
    }

    @Override
    public void setImageProcessor(ImageProcessor processor) {
        imgProcessor = processor;
    }

    @Override
    public ImageProcessor getImageProcessor() {
        return imgProcessor;
    }
}
