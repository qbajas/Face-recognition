package controllers;

import javax.swing.JFileChooser;
import views.FileChooser;

public class StartController {

	public void openFileChooser() {
		FileChooser fc = new FileChooser();
		fc.LoadFile();
	}

}
