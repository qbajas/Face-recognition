package views;

import Utils.MessageConsole;
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
import javax.swing.JScrollPane;

import org.encog.ml.data.MLDataSet;

import controllers.StartController;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JTextArea;

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

	
	public JPanel leftPanel = new JPanel();
	public JLabel yourImageLabel = new JLabel("");
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Neural Network Face Recognition System");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLoadAnImage = new JButton("1. Load an image");		
		final StartView view = this;
		btnLoadAnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.openFileChooser(view);
			}
		});
		btnLoadAnImage.setBounds(39, 395, 300, 23);
		frame.getContentPane().add(btnLoadAnImage);
		
		
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setBounds(39, 36, 300, 348);
		frame.getContentPane().add(leftPanel);
		leftPanel.setLayout(new BorderLayout(0, 0));
		yourImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		yourImageLabel.setIcon(new ImageIcon("img/anonymousAvatar.png"));
		leftPanel.add(yourImageLabel);
		
		JLabel lblYourImage = new JLabel("Your image:");
		lblYourImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourImage.setBounds(39, 11, 300, 14);
		frame.getContentPane().add(lblYourImage);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(445, 36, 300, 348);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon("img/anonymousAvatar.png"));
		panel_1.add(lblNewLabel_1);
		
		
		JLabel lblPersonFound = new JLabel("Person found:");
		lblPersonFound.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersonFound.setBounds(445, 11, 300, 14);
		frame.getContentPane().add(lblPersonFound);
		
		JButton btnFindAPerson = new JButton("2. Find this person !");
		btnFindAPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.findPerson();
			}
		});
		btnFindAPerson.setBounds(445, 395, 300, 23);
		frame.getContentPane().add(btnFindAPerson);
		
		JButton btnAdvancedSettings = new JButton("Advanced settings");
		btnAdvancedSettings.setBounds(39, 528, 706, 23);
		frame.getContentPane().add(btnAdvancedSettings);
		
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(39, 429, 706, 88);
		frame.getContentPane().add(scrollPane);
		activateConsole(scrollPane, textArea);
		
	}
	
	
	private void activateConsole(JScrollPane scrollPane, JTextArea textArea){
		frame.getContentPane().add( scrollPane );
		MessageConsole mc = new MessageConsole(textArea);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		//mc.setMessageLines(100);
	}
}
