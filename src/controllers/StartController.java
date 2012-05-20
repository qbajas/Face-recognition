package controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import views.FileChooser;
import views.StartView;

public class StartController {

	
	public void openFileChooser(StartView view) {
		FileChooser fc = new FileChooser();
		File f = fc.LoadFile();
		
		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(f);
			view.yourImageLabel.setIcon(new ImageIcon(myPicture));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Image not recognized");
			e.printStackTrace();			
		}
	}

}
