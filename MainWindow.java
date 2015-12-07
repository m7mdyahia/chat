package chatapp;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.List;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



public class MainWindow extends JFrame {

	public void update_message_box(String str)
	{
		textArea.setText(textArea.getText().trim()+"\n"+str);
	}
	public void update_list_of_users()
	{
		System.out.println("size should sent"+pe.list_of_users.size());
		list.removeAll();
		for(int i=0;i<pe.list_of_users.size();i++)
    	{
			   if(pe.list_of_users.get(i).is_online == true)
			      {
			      
			       list.addItem(pe.list_of_users.get(i).username+" (on)");
			     
			       }
			   else {
				   
			       list.addItem(pe.list_of_users.get(i).username+" (off)");

			   }
		 
	//	 list.add(pe.list_of_users.get(i).username +"online ="+ pe.list_of_users.get(i).is_online);
		 }
	}
	ArrayList<GroupWindow> GroupWindowList;
	
	private JPanel contentPane;
	private JTextField textField;
    JTextArea textArea = new JTextArea();
	List list = new List();
	Peer pe;
	JLabel l;
	String str;

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
	public MainWindow(String s) {
		this.str = s; 
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	JFrame frame = (JFrame)windowEvent.getSource();
		    	pe.exit();
		    	System.out.println("exited from server");
		    	frame.setVisible(false);
		    	frame.dispose();
		    }
		});
		
		GroupWindowList=new ArrayList<GroupWindow>();
		//this.p=p;
		pe= new Peer(s,this);
	    pe.start();
	    
	    
	    

	    
	   
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 449, 381);
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
		
		JButton btnNewButton = new JButton("Group Chat");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GroupWindow group=new GroupWindow(pe);
				GroupWindowList.add(group);
				group.setVisible(true);
			}
		});
		
		btnNewButton.setBounds(10, 24, 113, 23);
		contentPane.add(btnNewButton);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//	 System.out.println(list.getSelectedItem());
				
				String name_state = list.getSelectedItem();
			    String name = name_state.substring(0, name_state.indexOf(" "));
			    String state=name_state.substring(name_state.indexOf(" ")+3,name_state.length()-1);
			    if(Objects.equals(state, new String("n"))) pe.call_peer(name);
			    else JOptionPane.showMessageDialog(contentPane, "this user is offline");
			    
				
				
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
				
				textArea.setText(textArea.getText().trim()+"\n"+str+":"+textField.getText());
				pe.send_message_peer(str+":"+textField.getText());

                textField.setText("".trim());

				
			}

		});
		btnSend.setBounds(196, 234, 89, 23);
		contentPane.add(btnSend);
		
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
         
		update_list_of_users();
	
		
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
				// list.clear();
		//		update_list_of_users();
			//	for(int i=0;i<pe.list_of_users.size();i++)
		    ///	{
				// list.addItem(pe.list_of_users.get(i).username);}
				//System.out.println(pe.list_of_users.size());
			}
		});
		btnNewButton_2.setBounds(10, 308, 60, 23);
		contentPane.add(btnNewButton_2);
		
		
		
	}
	public void update_group_windows() {
		for (GroupWindow groupWindow : GroupWindowList) {
			groupWindow.update_groups();
			
		}
		
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