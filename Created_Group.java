package chatapp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;

import java.awt.Button;

import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Scrollbar;

public class Created_Group {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Created_Group window = new Created_Group();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Created_Group() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(28, 82, 261, 115);
	    frame.getContentPane().add(scrollPane);

	    JTextArea textArea = new JTextArea();
	    scrollPane.setViewportView(textArea);
	    textArea.setEditable(false);


		textField = new JTextField();
		textField.setBounds(28, 208, 261, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblGroupname = new JLabel("Group_Name");
		lblGroupname.setBounds(170, 26, 141, 14);
		frame.getContentPane().add(lblGroupname);
		
		Button button = new Button("Back");
		button.setBounds(10, 10, 70, 22);
		frame.getContentPane().add(button);
		
		Button button_1 = new Button("Send");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText(textArea.getText().trim()+"\n"+textField.getText());
				
			}
		});
		button_1.setBounds(219, 234, 70, 22);
		frame.getContentPane().add(button_1);
		
		
		
	}
}
