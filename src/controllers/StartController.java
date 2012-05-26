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

	// handles click on 'load image' from start view
	// returns chosen file
	public File openFileChooser(StartView view) {
		FileChooser fc = new FileChooser();
		File f = fc.LoadFile();

		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(f);
			view.yourImageLabel.setIcon(new ImageIcon(myPicture));
			System.out.println("Image loaded.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Image not recognized");
			e.printStackTrace();
		}
		return f;
	}

	// handles click on 'find a person' from start view
	public void findPerson(Icon icon) {

		BufferedImage img = new BufferedImage(icon.getIconWidth(),
				icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);

		if (ann == null)
			ann = createANN(100);
		
		int index = ann.getSubjectNbr(img);
		System.out.println("Recognized image number " + index);
	}

	public void openAdvancedSettings(StartView view) {

		AdvancedView frame = new AdvancedView();
		frame.setVisible(true);

	}

	public void train(int pcaSize, ANN.TrainMethod trainMethod) {
		if (ann == null)
			ann = createANN(pcaSize);
		
		ANNManager manager = new ANNManager();
		//train
		ann.train(trainMethod, true);
		manager.saveANN(ann);				
	}
	
	
	private ANN createANN(int pcaSize){
		ANNManager manager = new ANNManager();
		DataProcessor dataProcessor = new PCADataProcessor(pcaSize);
		ImageToVectorProcessor imageProcessor = new ImageToVectorProcessor(true);
		ANN network = manager.getANN(imageProcessor, dataProcessor, false);
		return network;
	}

}
