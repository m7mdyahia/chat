package chatapp;
import java.awt.TrayIcon.MessageType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class clientHandler extends Thread {

    Socket user;
    volatile Peer_chat_server chat_server;

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
            
            dos.writeObject(new Message(Message.MsgType.Enter_Name));
            
            
            User_Message um = (User_Message)dis.readObject();
            User user_identification = new User(um.user); 
            user_identification.is_online = true;	
            user_identification.ip = user.getInetAddress();
            //user_identification.socket=user;
            
            System.out.println("user arrived "+user_identification.username);
            chat_server.user_List.add(user_identification);
            System.out.println("I added u to list");
            
            while (true) {
           
                                         
               Message request = (Message)dis.readObject();
                System.out.println("I got a new request");
                
              switch (request.msg) {
              case Bye:
              {
            	  user_identification.is_online=false;
              }
              case List_Users:
              {
                  System.out.println("here i'm writing your list");
            	  dos.writeObject(new ListofUseres(chat_server.user_List));
            	  for (User user : chat_server.user_List) {
            		  System.out.print(user.username+", ");	
				}
            	 
                  break;
              }
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

              case group_chat_message:
            	  broadcast_messsage_send sent_message = (broadcast_messsage_send)dis.readObject();
            	  new BroadcastThread(sent_message,chat_server,user_identification.username).start();

			default:
				break;
			}
       
                
                
//                //Checks must be performed
//                //user select to create a group chat  or chat to another peer
//                dos.writeUTF("Your Payment was successful \nDo you want to perform another payment[Y/N] ?");
                String Choice = "l";
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
	List<User> user_List =Collections.synchronizedList(new ArrayList<User>()) ;
	List<available_groups> available_groups_list=Collections.synchronizedList(new ArrayList<available_groups>());
	List<clientHandler> threadlist=Collections.synchronizedList(new ArrayList<clientHandler>());

 
    public void start() {

        // TODO code application logic here
        try {
            
            ServerSocket sv = new ServerSocket(1234);
            while (true) {
                System.out.println("Hello from server");

                Socket peer= sv.accept();
                System.out.println("New peer Arrived");
                clientHandler user_thread = new clientHandler(peer,this);
                threadlist.add(user_thread);
                System.out.println("Hey");
                user_thread.start();
               

            }

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }

    }

	public void broadcast(String data, String group, User user_identification) {
		for (available_groups available_groups : available_groups_list) {
			if(group==available_groups.name)
			{
				available_groups.broadcast(data,user_identification);
			}
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
	//Dictionary<String, User> group_dic;
	
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

	public boolean  broadcast(String msg, User user_identification)
	{
		for (User user : group_useres) {
			if(user.equals(user_identification))
			{
				;
				
			}
			
		}
		return false;
	}
	
	

	
}

class BroadcastThread extends Thread
{
	String messag_text;
	available_groups group;
	String sender_name;
	 
	public BroadcastThread(String messag_text, available_groups group) {
		super();
		this.messag_text = messag_text;
		this.group = group;
	}



	public BroadcastThread(broadcast_messsage_send sent_message,
			Peer_chat_server chat_server, String sender_name) {
		this.messag_text=sent_message.data;
		this.sender_name = sender_name;
		
		for (available_groups g : chat_server.available_groups_list) {
			if(g.name == sent_message.group_name);
			group=g;
		}
	}


	public void run()
	{
		ObjectOutputStream dos ; 
		for (User u : group.group_useres )
		{
			if(u.is_online)//By medo
			{
//				try 
//				{
//					dos= new ObjectOutputStream(u.socket.getOutputStream());
//					dos.writeObject(new broadcast_messsage_send(messag_text,group.name,sender_name));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
	}
	
	
}

