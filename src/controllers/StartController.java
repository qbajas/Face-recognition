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
	public void findPerson(){
		//TODO
	}

}
