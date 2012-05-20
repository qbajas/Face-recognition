package views;

import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.*;

public class FileChooser {
	
	    JFileChooser jfc;
	    JFrame window;
	    
	    public FileChooser()
	    {
	        window=new JFrame();
	        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        jfc=new JFileChooser();
	    }

	    
	    public File LoadFile()
	    {
	        //FileFilter filter = new FileNameExtensionFilter();
	        //jfc.addChoosableFileFilter(filter);
	        int returnVal=jfc.showOpenDialog(window);
	        if (returnVal==JFileChooser.OPEN_DIALOG)
	        return jfc.getSelectedFile();
	        else return null;
	    }

	  
}
