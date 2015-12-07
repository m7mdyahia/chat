package chatapp;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class Login {

	private JFrame frame;
	private JTextField textField;
	private ArrayList<GroupWindow> GroupWindowList;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
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
	public Login() {
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
		JRadioButton Server, Client;
		ButtonGroup G;
		
		
		
		Server = new JRadioButton("Server");
		Server.setBounds(308, 194, 103, 25);
		frame.getContentPane().add(Server);
		
		Client = new JRadioButton("Client");
		Client.setBounds(74, 194, 109, 25);
		frame.getContentPane().add(Client);
		
		
		
		G =new ButtonGroup();
		G.add(Server);
		G.add(Client);
		
		textField = new JTextField();
		textField.setBounds(172, 109, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Server.isSelected())
				  {				  

					Server server=new Server();  frame.setVisible(false); server.setVisible(true);
				    Peer_chat_server sv = new Peer_chat_server();
				    Thread thread = new Thread() {
				        public void run() {
				            sv.start();
				            
				        }
				    };
				    thread.start();
				  }
				  else if(Client.isSelected())
				  { 

					  MainWindow main=new MainWindow(textField.getText());  
					  frame.setVisible(false);
					  main.setVisible(true);
				    
				    }
				 
			}
		});
		btnLogin.setBounds(172, 132, 89, 23);
		frame.getContentPane().add(btnLogin);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(186, 92, 86, 14);
		frame.getContentPane().add(lblUsername);
		
		
		
		
		
	}
	
	public void back() {frame.setVisible(true);}

}


