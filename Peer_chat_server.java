package chatapp;
import java.awt.TrayIcon.MessageType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import chatapp.Message.MsgType;

class clientHandler extends Thread {

    private Socket user;
    Peer_chat_server chat_server;

    // constructor
    public clientHandler(Socket client,Peer_chat_server chat_server) {
        this.user = client;
        this.chat_server = chat_server;
    }

    @Override
    public void run() {
        try {
            //Create IO Streams
            ObjectOutputStream dos = new  ObjectOutputStream(user.getOutputStream());
            ObjectInputStream dis = new ObjectInputStream(user.getInputStream());
            
            while (true) {
                dos.writeObject(new Message(Message.MsgType.Enter_Name));
                
                
                User_Message um = (User_Message)dis.readObject();
                User user_identification = new User(um.user); 
                user_identification.is_online = true;	
                user_identification.ip = user.getInetAddress();
                
                 System.out.println(user_identification.username);
                chat_server.user_List.add(user_identification);        
                                         
               Message request = (Message)dis.readObject();
        
                
              switch (request.msg) {
              case Create_Group:
              {
           	  
            	  chat_server.available_groups_list.add(new available_groups(request.data,user_identification))
				;
				break;		
              }
              case List_Groups:
            	  {
            		  dos.writeObject(new ListofGroups(chat_server.available_groups_list));
                       break;
             }
              case Join_Group:
            	   chat_server.join(request.data,user_identification);
            	  break;
              case Conv_Msg:
            	  chat_server.broadcast(request.data,user_identification,)
			default:
				break;
			}
       
                
                
                //Checks must be performed
                //user select to create a group chat  or chat to another peer
                dos.writeUTF("Your Payment was successful \nDo you want to perform another payment[Y/N] ?");
                String Choice = dis.readUTF();
                if (Choice.equalsIgnoreCase("N")) {
                    dos.writeUTF("Bye");
                    break;
                }

            }
            //Close/release resources
            dis.close();
            dos.close();
            user.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

public class Peer_chat_server {
	ArrayList<User> user_List;
	ArrayList<available_groups> available_groups_list;

    /**
     * @param args the command line arguments
     */
	
    public  void start() {
        // TODO code application logic here
        try {
            
            ServerSocket sv = new ServerSocket(1234);
            while (true) {
                
                Socket peer= sv.accept();
                System.out.println("New peer Arrived");
                clientHandler user_thread = new clientHandler(peer,this);
                user_thread.start();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

	public void join(String data,User user) {
		// TODO Auto-generated method stub
		
		for (Iterator iterator = available_groups_list.iterator(); iterator
				.hasNext();) {
			
			available_groups ag = (available_groups) iterator
					.next();
			
			if(ag.name==data)
			{ 
				ag.group_useres.add(user);
				break;
			}
		}
		
	}

}



class available_groups
{
	String name;
	ArrayList<User> group_useres;
	
	
	public available_groups(String name) {
		super();
		this.name = name;
		this.group_useres = new ArrayList<User>();
	}
	public available_groups(String name,User creator) {
		super();
		this.name = name;
		this.group_useres = new ArrayList<User>();
		this.group_useres.add(creator);
	}

	public boolean  broadcast(String msg)
	{
		return false;
	}
	
	

	
}







