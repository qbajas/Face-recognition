package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import Utils.MessageConsole;

public class AdvancedView extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AdvancedView() {
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnTrain = new JButton("Train the network");
		btnTrain.setBounds(10, 170, 764, 23);
		contentPane.add(btnTrain);
		
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(39, 429, 706, 88);
		getContentPane().add(scrollPane);
		MessageConsole.activateConsole(scrollPane, textArea);
		
	}

}
