package chatapp;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.List;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class Main extends JFrame {

	public void update_message_box(String str)
	{
		textArea.setText(textArea.getText().trim()+"\n"+str);
	}
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_2;
    JTextArea textArea = new JTextArea();
	List list = new List();
	Peer pe;
	JLabel l;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Main frame = new Main();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main(String s) {
		
		
		//this.p=p;
		 pe= new Peer(s,this);
	     pe.start();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle(s);

		
		JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(28, 82, 261, 115);
	    contentPane.add(scrollPane);

	    scrollPane.setViewportView(textArea);
	    textArea.setEditable(false);
		
		JButton btnNewButton = new JButton("Create Group");
		btnNewButton.setBounds(10, 27, 113, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Find Group");
		btnNewButton_1.setBounds(10, 50, 113, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//	 System.out.println(list.getSelectedItem());
				
				pe.call_peer(list.getSelectedItem());
				
				
			}
		});
		btnConnect.setBounds(335, 214, 89, 23);
		contentPane.add(btnConnect);
		
		textField = new JTextField();
		textField.setBounds(28, 203, 261, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				textArea.setText(textArea.getText().trim()+"\n"+s+":"+textField.getText());
				pe.send_message_peer(s+":"+textField.getText());
//                try {
//
//					pe.                	pe.pc.dos.reset();pc.dos.writeObject(new Message(Message.MsgType.Conv_Msg,textField.getText()));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                
                textField.setText("".trim());

				
			}

		});
		btnSend.setBounds(200, 227, 89, 23);
		contentPane.add(btnSend);
		
		textField_2 = new JTextField();
		textField_2.setBounds(137, 28, 86, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblGroupName = new JLabel("Group Name");
		lblGroupName.setBounds(147, 11, 86, 14);
		contentPane.add(lblGroupName);
		
		list.setBounds(325, 26, 99, 182);
		contentPane.add(list);
		
		//Timer timer = new Timer(1000 ,new MyTimerActionListener());
       // timer.setRepeats(true);
       // timer.start();
		
//		l = new JLabel("");
//		l.setBounds(137,3,72,14);
//		contentPane.add(l);
//		l.setText(s);
		
		
		  pe.update_me();
		
		for(int i=0;i<pe.list_of_users.size();i++)
    	{
		 
		 list.addItem(pe.list_of_users.get(i).username);}
	     
         
		
		//list.addItem("ops");
		//list.addItem("ool");
		
		Label label = new Label("Online Users");
		label.setBounds(335, 3, 99, 22);
		contentPane.add(label);
		
		Scrollbar scrollbar = new Scrollbar();
		scrollbar.setBounds(407, 27, 17, 50);
		contentPane.add(scrollbar);
		
		JButton btnNewButton_2 = new JButton("R");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 pe.update_me();
				 list.clear();

				for(int i=0;i<pe.list_of_users.size();i++)
		    	{
				 list.addItem(pe.list_of_users.get(i).username);}
				//System.out.println(pe.list_of_users.size());
			}
		});
		btnNewButton_2.setBounds(0, 238, 39, 23);
		contentPane.add(btnNewButton_2);
		
	}
}

//class MyTimerActionListener implements ActionListener {
	//  public void actionPerformed(ActionEvent e) {
		//  Main.p.update_me();
		 // for(int i=0;i<Main.p.list_of_users.size();i++)
	    //	{
			 
			// Main.list.addItem(Main.p.list_of_users.get(i).username);}
	//  }
	//}