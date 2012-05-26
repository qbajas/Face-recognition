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
import javax.swing.UnsupportedLookAndFeelException;

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
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import ann.ANN.TrainMethod;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;

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
	public JLabel personFoundLabel = new JLabel("");
	private JTextField textField;
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame("Neural Network Face Recognition System");
		frame.setBounds(100, 100, 800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLoadAnImage = new JButton("1. Load an image");		
		final StartView view = this;
		btnLoadAnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.openFileChooser(view);
			}
		});
		btnLoadAnImage.setBounds(39, 347, 300, 23);
		frame.getContentPane().add(btnLoadAnImage);
		
		
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setBounds(39, 36, 300, 300);
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
		panel_1.setBounds(445, 36, 300, 300);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		
		personFoundLabel.setHorizontalAlignment(SwingConstants.CENTER);
		personFoundLabel.setIcon(new ImageIcon("img/anonymousAvatar.png"));
		panel_1.add(personFoundLabel);
		
		
		JLabel lblPersonFound = new JLabel("Person found:");
		lblPersonFound.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersonFound.setBounds(445, 11, 300, 14);
		frame.getContentPane().add(lblPersonFound);
		
		JButton btnFindAPerson = new JButton("2. Find this person !");
		btnFindAPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.findPerson(view);
			}
		});
		btnFindAPerson.setBounds(445, 347, 300, 23);
		frame.getContentPane().add(btnFindAPerson);
		
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 499, 764, 152);
		frame.getContentPane().add(scrollPane);
		MessageConsole.activateConsole(scrollPane, textArea);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setHorizontalAlignment(SwingConstants.CENTER);
		lblOutput.setBackground(new Color(255, 248, 220));
		scrollPane.setColumnHeaderView(lblOutput);
		
	
		JLabel lblTrainingMethod = new JLabel("Training method");
		lblTrainingMethod.setBounds(39, 428, 110, 14);
		frame.getContentPane().add(lblTrainingMethod);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(TrainMethod.values()));
		comboBox.setSelectedIndex(1);
		comboBox.setBounds(39, 447, 203, 20);
		frame.getContentPane().add(comboBox);
		
		JLabel lblPcaSize = new JLabel("PCA size (50-200)");
		lblPcaSize.setBounds(278, 428, 145, 14);
		frame.getContentPane().add(lblPcaSize);
		
		textField = new JTextField();
		textField.setText("100");
		textField.setBounds(278, 447, 48, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnTrainNetwork = new JButton("Train network");
		btnTrainNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.train(Integer.parseInt(textField.getText()), (TrainMethod) comboBox.getSelectedItem());
			}
		});
		btnTrainNetwork.setBounds(445, 446, 300, 23);
		frame.getContentPane().add(btnTrainNetwork);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(128, 128, 128)));
		panel.setBackground(new Color(255, 248, 220));
		panel.setBounds(-15, 410, 814, 78);
		frame.getContentPane().add(panel);
		
	}
}
