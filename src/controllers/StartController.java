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

import Utils.Config;
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
			System.out.println("Image " + f.getName() + " loaded.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Image not recognized");
		}
		return f;
	}

	// handles click on 'find a person' from start view
	public void findPerson(StartView view) {
		// picture was not loaded, cant find it
		if (loadedPicture == null) {
			System.out.println("Please load a picture first !");
			return;
		}
		// network was not trained, cant use it
		if (ann == null) {
			// System.out.println("You have to train the network first !");
			// return;
			ann = createANN(100, false);
		}
		int index = ann.getSubjectNbr(loadedPicture);
		if (index != 0) {
			System.out.println("Recognized person number " + index);

			String path = Config.dataPath + "/Subject"
					+ String.format("%02d", index) + "/A_"
					+ String.format("%02d", index) + "_0.Jpg";
			try {
				BufferedImage img = ImageIO.read(new File(path));
				view.personFoundLabel.setIcon(new ImageIcon(img));
			} catch (IOException e) {
				System.out.println("Error - image not found. Path: " + path);
				//e.printStackTrace();
			}
		}
		else
			System.out.println("Image not recognized.");
	}

	public void openAdvancedSettings(StartView view) {

		AdvancedView frame = new AdvancedView();
		frame.setVisible(true);

	}

	public void train(int pcaSize, final ANN.TrainMethod trainMethod) {
		if (ann == null)
			ann = createANN(pcaSize, false); // dont create a new ann if already
												// exists

		final ANNManager manager = new ANNManager();
		// train
		new Thread(new Runnable() {

			@Override
			public void run() {
				ann.train(trainMethod, true);
				manager.saveANN(ann);
			}
		}).start();

	}

	private ANN createANN(int pcaSize, boolean forceNew) {
		ANNManager manager = new ANNManager();
		DataProcessor dataProcessor = new PCADataProcessor(pcaSize);
		ImageToVectorProcessor imageProcessor = new ImageToVectorProcessor(true);
		ANN network = manager.getANN(imageProcessor, dataProcessor, forceNew);
		return network;
	}

}
