package chatapp;

import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Group extends JFrame {

	private JPanel contentPane;
	String group_name;
	 JTextArea textArea;
	private JTextField textField;
	private JTextField textField_1;
	List list;
	Peer pe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				//	Group frame = new Group();
				//	frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param pe 
	 */
	public void update_groups()
	{
		pe.update_me();
		list.clear();
		for(int i=0;i<pe.group_list.size();i++)
    	{
		 list.addItem(pe.group_list.get(i).name);
		 }
	}
	
	public Group(Peer pe) {
		this.pe=pe;
		pe.group=this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(28, 204, 261, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(textArea.getText().trim()+"\n"+"me"+":"+textField.getText());
				pe.send_group_message(group_name, textField.getText());

                textField.setText("".trim());
			}
		});
		btnSend.setBounds(200, 227, 89, 23);
		contentPane.add(btnSend);
		
		list = new List();
		list.setBounds(314, 10, 110, 172);
		contentPane.add(list);
	
		update_groups();

		
		JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(28, 82, 261, 115);
	    contentPane.add(scrollPane);

	     textArea = new JTextArea();
	    scrollPane.setViewportView(textArea);
	    textArea.setEditable(false);
	    
	    JButton btnCreateGroup = new JButton("Create Group");
	    btnCreateGroup.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(!textField_1.getText().equals(""))
	    		{
	    		pe.creategroup(textField_1.getText());
	    		update_groups();
	    		group_name=textField_1.getText();
	    		}

	    		
	    	}
	    });
	    btnCreateGroup.setBounds(10, 11, 110, 23);
	    contentPane.add(btnCreateGroup);
	    
	    textField_1 = new JTextField();
	    textField_1.setBounds(130, 12, 86, 20);
	    contentPane.add(textField_1);
	    textField_1.setColumns(10);
	    
	    JButton btnNewButton = new JButton("Join");
	    btnNewButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		pe.joingroup(list.getSelectedItem());
	    		group_name=list.getSelectedItem();
	    	}
	    });
	    btnNewButton.setBounds(324, 188, 89, 23);
	    contentPane.add(btnNewButton);
	    
	    JButton btnR = new JButton("R");
	    btnR.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		update_groups();
			
	    	}
	    });
	    btnR.setBounds(385, 227, 39, 23);
	    contentPane.add(btnR);
	}

	public void recived_group_message(broadcast_messsage_send group_message) {
		textArea.setText(textArea.getText().trim()+"\n"+group_message.sender_name+":"+group_message.data);
		
	}
}
