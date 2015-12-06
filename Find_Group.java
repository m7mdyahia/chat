package chatapp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.List;
import java.awt.Scrollbar;
import java.awt.Button;

import javax.swing.JTextArea;

public class Find_Group {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Find_Group window = new Find_Group();
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
	public Find_Group() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("deprecation")
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 485, 357);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		List list = new List();
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        
		            System.out.println("close");
		        
		    }
		});

		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(list.getSelectedItem());

			}
		});
		
		   
		btnJoin.setBounds(321, 228, 100, 23);
		frame.getContentPane().add(btnJoin);
		
		textField = new JTextField();
		textField.setBounds(28, 205, 261, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(28, 82, 261, 115);
	    frame.getContentPane().add(scrollPane);

	    JTextArea textArea = new JTextArea();
	    scrollPane.setViewportView(textArea);
	    textArea.setEditable(false);

	    
		list.setBounds(311, 23, 110, 199);
		frame.getContentPane().add(list);
		list.addItem("lol");
		list.addItem("a2ee");
		
		
		Scrollbar scrollbar = new Scrollbar();
		scrollbar.setBounds(413, 23, 8, 50);
		frame.getContentPane().add(scrollbar);
		
		Button button = new Button("Send");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button.setBounds(219, 228, 70, 22);
		frame.getContentPane().add(button);
		
		Button button_1 = new Button("Back");
		button_1.setBounds(10, 10, 70, 22);
		frame.getContentPane().add(button_1);
		
		Scrollbar scrollbar_1 = new Scrollbar();
		scrollbar_1.setBounds(404, 23, 17, 50);
		frame.getContentPane().add(scrollbar_1);
		
		JButton btnCreatGroup = new JButton("Creat Group");
		btnCreatGroup.setBounds(321, 262, 100, 23);
		frame.getContentPane().add(btnCreatGroup);
	}
}
