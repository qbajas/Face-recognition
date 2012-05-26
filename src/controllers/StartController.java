package controllers;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import data.DataProcessor;
import data.ImageToVectorProcessor;
import data.PCADataProcessor;

import ann.ANN;
import ann.ANNManager;

import views.AdvancedView;
import views.FileChooser;
import views.StartView;

public class StartController {
	
	private ANN ann;
	private BufferedImage loadedPicture;

	// handles click on 'load image' from start view
	// returns chosen file
	public File openFileChooser(StartView view) {
		FileChooser fc = new FileChooser();
		File f = fc.LoadFile();

		try {
			loadedPicture = ImageIO.read(f);
			view.yourImageLabel.setIcon(new ImageIcon(loadedPicture));
			System.out.println("Image " +f.getName()+ " loaded.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Image not recognized");
			e.printStackTrace();
		}
		return f;
	}

	// handles click on 'find a person' from start view
	public void findPerson() {

		if (loadedPicture==null)
		{
			System.out.println("Please load a picture first !");
			return;
		}

                new Thread(new Runnable() {

            @Override
            public void run() {
                if (ann == null)
			ann = createANN(100, false);
		
		int index = ann.getSubjectNbr(loadedPicture);
		System.out.println("Recognized image number " + index);
            }
        }).start();

	}

	public void openAdvancedSettings(StartView view) {

		AdvancedView frame = new AdvancedView();
		frame.setVisible(true);

	}

	public void train(int pcaSize,final ANN.TrainMethod trainMethod) {
		if (ann == null)
			ann = createANN(pcaSize,true);
		
		final ANNManager manager = new ANNManager();
		//train
                new Thread(new Runnable() {

            @Override
            public void run() {
                ann.train(trainMethod, true);
		manager.saveANN(ann);	
            }
        }).start();
					
	}
	
	
	private ANN createANN(int pcaSize, boolean forceNew){
		ANNManager manager = new ANNManager();
		DataProcessor dataProcessor = new PCADataProcessor(pcaSize);
		ImageToVectorProcessor imageProcessor = new ImageToVectorProcessor(true);
		ANN network = manager.getANN(imageProcessor, dataProcessor, forceNew);
		return network;
	}

}
