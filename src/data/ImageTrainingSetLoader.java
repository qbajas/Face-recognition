package data;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLData;

/**
 *
 * @author Michal
 */
public class ImageTrainingSetLoader implements DataLoader{
    
    private static String[] imagesPath =
        {
            "_0.Jpg",
            "_+05.Jpg",
            "_+25.Jpg", 
            "_+45.Jpg", 
            "_+75.Jpg", 
            
            "_-05.Jpg",
            "_-25.Jpg", 
            "_-45.Jpg", 
            "_-75.Jpg",
            
            "_+5.Jpg",
            "_-5.Jpg"
        };
    
    private ImageProcessor imgProcessor;
    private DataProcessor dataProcessor;

    
    public ImageTrainingSetLoader(ImageProcessor imgProcessor, DataProcessor dataProcessor) {
        this.imgProcessor = imgProcessor;
        this.dataProcessor = dataProcessor;
    }

    public ImageTrainingSetLoader() {
        imgProcessor = new ImageToVectorProcessor(true);
        dataProcessor = new DataProcessor();
    }
    
    @Override
    public MLData loadData(String source) {
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
                    if(name.endsWith(s))
                        return true;
                }
                return false;
            }
        };
        
        File images[];
        int subjectNbr;
        int subjectsCount = subFolders.length;
        
        for(File folder:subFolders){
            images = folder.listFiles(jpgFilter);
            subjectNbr = Integer.parseInt( folder.getName().replace("Subject", "") );
            
            double output[] = new double[subjectsCount];
            output[subjectNbr-1] = 1.0;
            
            System.out.println(folder.getName());

            for(File image:images){
                outputs.add(output);
                try {
                    inputs.add(imgProcessor.process(ImageIO.read(image)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
   
        throw new RuntimeException("not supported");
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
