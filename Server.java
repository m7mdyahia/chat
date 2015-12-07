package chatapp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.List;
import java.io.IOException;
import java.util.Objects;

public class Server extends JFrame {

	private JPanel contentPane;
	List list = new List();
	Peer_chat_server server;


	/**
	 * Launch the application.
	 */
	
	public void update_list_of_users()
	{
		list.removeAll();
		for(int i=0;i<server.user_List.size();i++)
    	{
			   if(server.user_List.get(i).is_online == true)
			      {
			      
			       list.addItem(server.user_List.get(i).username+" (on)");
			     
			       }
			   else {
				   
			       list.addItem(server.user_List.get(i).username+" (off)");

			   }
		 
		 }
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param sv 
	 */
	public Server(Peer_chat_server sv) {
		this.server = sv ; 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JButton btnBack = new JButton("Logout");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				Login login=new Login();
				login.back();
			}
		});
		btnBack.setBounds(10, 11, 89, 23);
		contentPane.add(btnBack);
		
		list.setBounds(153, 44, 133, 154);
		contentPane.add(list);
		
		JButton btnKickOff = new JButton("Kick off");
		btnKickOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String name_state = list.getSelectedItem();
			    String name = name_state.substring(0, name_state.indexOf(" "));
			    String state=name_state.substring(name_state.indexOf(" ")+3,name_state.length()-1);
			    if(Objects.equals(state, new String("n"))){
			    	
			    	for(int i=0;i<server.user_List.size();i++)
			    	{
						   if(server.user_List.get(i).username.equalsIgnoreCase(name) )
						      {
						      System.out.println("I kicked"+ name);
							   server.user_List.get(i).is_online = false;	
							   try {
								server.socket_map.get(server.user_List.get(i)).dis.close();
								  server.socket_map.get(server.user_List.get(i)).dos.close();
								   server.socket_map.get(server.user_List.get(i)).user.close();
								   server.socket_map.get(server.user_List.get(i)).interrupt();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							 

						       }
						 
					 
					 }
			    	
			    }
			   else JOptionPane.showMessageDialog(contentPane, "this user is already offline");	
			    update_list_of_users();
			    
			    
			    
				
			}
		});
		btnKickOff.setBounds(174, 217, 97, 25);
		contentPane.add(btnKickOff);
		
		JButton btnR = new JButton("R");
		btnR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				update_list_of_users();
			}
		});
		btnR.setBounds(10, 217, 41, 25);
		contentPane.add(btnR);
	}
}
