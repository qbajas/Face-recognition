package controllers;

import Utils.Config;
import ann.ANN;
import ann.ANNManager;
import data.ImageToVectorProcessor;
import data.PCADataProcessor;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import views.StartView;

public class Main {

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
            
            /////////////TEST/////////////////////////////////////////////////////////////
		
//          ANNManager annManager = new ANNManager();
//          ANN ann = annManager.getANN(new ImageToVectorProcessor(true), new PCADataProcessor(100), false);
//          ann.setThreshold(0.7);
//          test(Config.dataPath,Config.falseDataPath,ann);
//          
    
            ////////////////////////////////////////////////////////////////////////
            
          
        final StartController sc = new StartController();
            
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartView window = new StartView(sc);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
        
        public static void test(String source, String falseSource, ANN ann) {
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

        File images[];
        
        System.out.println("Testing...");
        for (File folder : subFolders) {
            System.out.println(folder.getName());
            images = folder.listFiles(jpgFilter);

            for (File image : images) {
                try {
                    System.out.println(image.getName() +"  subject= "+ann.getSubjectNbr(ImageIO.read(image)) );
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
           
            }
        }
       
    }
}
