package views;

import ann.ANNManager;
import data.DataProcessor;
import data.ImageToVectorProcessor;
import data.ImageTrainingSetLoader;
import data.PCADataProcessor;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.encog.ml.data.MLDataSet;

import controllers.StartController;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class StartView {

	public JFrame frame;	
	private StartController controller;


	/**
	 * Create the application.
	 */
	public StartView(StartController c) {
		initialize();
		controller = c;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLoadAnImage = new JButton("Load an image");
		btnLoadAnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.openFileChooser();
			}
		});
		btnLoadAnImage.setBounds(39, 395, 300, 23);
		frame.getContentPane().add(btnLoadAnImage);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(39, 36, 300, 348);
		frame.getContentPane().add(panel);
		
		JLabel lblYourImage = new JLabel("Your image:");
		lblYourImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourImage.setBounds(39, 11, 300, 14);
		frame.getContentPane().add(lblYourImage);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(445, 36, 300, 348);
		frame.getContentPane().add(panel_1);
		
		JLabel lblPersonFound = new JLabel("Person found:");
		lblPersonFound.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersonFound.setBounds(445, 11, 300, 14);
		frame.getContentPane().add(lblPersonFound);
		
		JButton btnFindAPerson = new JButton("Find a person !");
		btnFindAPerson.setBounds(445, 395, 300, 23);
		frame.getContentPane().add(btnFindAPerson);
	}
}
