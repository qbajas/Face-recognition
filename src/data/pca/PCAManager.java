package data.pca;

import java.io.*;

/**
 *
 * @author Michal
 */
public class PCAManager {

    String PCAPath;

    public PCAManager(String searchPath) {
        PCAPath = searchPath;
    }

    public PCA getPCA(double[][] input, boolean recalculatePCA) {
        return getPCA(input, input.length, recalculatePCA);
    }

    public PCA loadPCA(int length){
        File file = new File(PCAPath + File.separatorChar + "PCA" + length + ".ser");
        PCA pca;
        if (file.exists()) {

            try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(fileIn)) {
                System.out.println("Loading PCA Object...");
                pca = (PCA) in.readObject();
                return pca;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                System.out.println("Can't load... Recalculating...");
            }
        }
        return null;
    }
    
    public PCA getPCA(double[][] input, int length, boolean recalculatePCA) {
        File file = new File(PCAPath + File.separatorChar + "PCA" + length + ".ser");
        PCA pca;
        if (!recalculatePCA && file.exists()) {

            try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(fileIn)) {
                System.out.println("Loading PCA Object...");
                pca = (PCA) in.readObject();
                return pca;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                System.out.println("Can't load... Recalculating...");
            }
        }

        System.out.println("Calculating PCA");
        pca = new PCA(input, length);
        
        try (FileOutputStream fileOut = new FileOutputStream(file); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            System.out.println("Saving PCA...");
            out.writeObject(pca);
        } catch (IOException e) {
            System.out.println("Can't save PCA");
            e.printStackTrace();
        }
        return pca;
    }
}
