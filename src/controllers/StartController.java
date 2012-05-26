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
		}
		return f;
	}

	// handles click on 'find a person' from start view
	public void findPerson() {
		// picture was not loaded, cant find it
		if (loadedPicture==null)
		{
			System.out.println("Please load a picture first !");
			return;
		}
		//network was not trained, cant use it
		if (ann == null)
		{
			System.out.println("You have to train the network first !");
			return;
		}
		int index = ann.getSubjectNbr(loadedPicture);
		System.out.println("Recognized image number " + index);
	}

	public void openAdvancedSettings(StartView view) {

		AdvancedView frame = new AdvancedView();
		frame.setVisible(true);

	}

	public void train(int pcaSize, ANN.TrainMethod trainMethod) {
		if (ann == null)
			ann = createANN(pcaSize,false); //dont create a new ann if already exists
		
		ANNManager manager = new ANNManager();
		//train, force training
		ann.train(trainMethod, true);
		manager.saveANN(ann);				
	}
	
	
	private ANN createANN(int pcaSize, boolean forceNew){
		ANNManager manager = new ANNManager();
		DataProcessor dataProcessor = new PCADataProcessor(pcaSize);
		ImageToVectorProcessor imageProcessor = new ImageToVectorProcessor(true);
		ANN network = manager.getANN(imageProcessor, dataProcessor, forceNew);
		return network;
	}

}
