package controllers;

import java.awt.EventQueue;

import views.StartView;
import ann.ANNManager;
import data.DataProcessor;
import data.ImageToVectorProcessor;

public class Main {

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
            
            /////////////TEST/////////////////////////////////////////////////////////////
		/*
          ANNManager ann = new ANNManager(new DataProcessor(), new ImageToVectorProcessor(true));
          ann.getANN(8480, 100, 40, true);
          ann.train(ANNManager.TrainMethod.BackPropagation);
          */
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
