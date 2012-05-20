package data;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;

/**
 * Klasa do wczytywania wszystkich zdjec twarzy z katalogu bazy obrazow, pogrupowanych
 * w foldery odpowiadajace poszczegolnym osobom
 * @author Michal
 */
public class ImageTrainingSetLoader implements DataLoader{
    
    private MLDataSet trainingSet;
    private MLDataSet testSet;
    private MLDataSet generalizationSet;

    private static String[] imagesPath ={
       "_0.Jpg", "_+05.Jpg", "_+25.Jpg", "_+45.Jpg",  "_+75.Jpg", "_-05.Jpg", 
       "_-25.Jpg", "_-45.Jpg", "_-75.Jpg",  "_+5.Jpg","_-5.Jpg"
    };
    
    private ImageProcessor imgProcessor;
    private DataProcessor dataProcessor;
    
    /**
     * Umożliwia ustawienie dodatkowych
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
    
    @Override
    public void loadData(String source) {
        File mainFolder = new File(source);
        File subFolders[] = mainFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        
        LinkedList<double[]> inputs = new LinkedList<>();
        LinkedList<double[]> outputs = new LinkedList<>();
        
        FileFilter jpgFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                for(String s:imagesPath){
                    if(name.toLowerCase().startsWith("a") && name.toLowerCase().endsWith(s.toLowerCase())){
                        return true;
                    }
                }
                return false;
            }
        };
        
        File images[];
        int subjectNbr;
        int subjectsCount = subFolders.length;
        
        for(File folder:subFolders){
            subjectNbr = Integer.parseInt( folder.getName().replace("Subject", "") );
            System.out.println(folder.getName());
            images = folder.listFiles(jpgFilter);

            double output[] = new double[subjectsCount];
            output[subjectNbr-1] = 1.0;
            
            for(File image:images){
                
                outputs.add(output);
                try {
                    inputs.add(imgProcessor.process(ImageIO.read(image)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        
        if(inputs.size()!=outputs.size())
            throw new RuntimeException("inputs!=outputs");
        
        double [][] annInputs = new double[inputs.size()][];
        double [][] annOutputs = new double[outputs.size()][];
        
        inputs.toArray(annInputs);
        outputs.toArray(annOutputs);
        
        annInputs = dataProcessor.processData(annInputs);
        
       trainingSet = new BasicMLDataSet(annInputs, annOutputs);        
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

    @Override
    public void ShuffleData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MLDataSet getTrainingSet() {
        loadData("dataSet");
        return trainingSet;
    }

    @Override
    public MLDataSet getGeneralizationSet() {
        return generalizationSet;
    }

    @Override
    public MLDataSet getTestSet() {
        return testSet;
    }
}
