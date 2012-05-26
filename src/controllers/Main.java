package controllers;

import ann.ANN;
import ann.ANNManager;
import data.ImageToVectorProcessor;
import data.PCADataProcessor;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import views.StartView;

public class Main {

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
            
            /////////////TEST/////////////////////////////////////////////////////////////
		
          ANNManager annManager = new ANNManager();
          ANN ann = annManager.getANN(new ImageToVectorProcessor(true), new PCADataProcessor(300), true);
          ann.train(ANN.TrainMethod.ResilentPropagation, true);
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
          annManager.saveANN(ann);
      
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
	
}
