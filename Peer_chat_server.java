package chatapp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class clientHandler extends Thread {

    Socket user;
    Peer_chat_server chat_server;
    User user_identification;
    ObjectOutputStream dos;
    ObjectInputStream dis;
    // constructor
    public clientHandler(Socket client,Peer_chat_server chat_server) {
        this.user = client;
        this.chat_server = chat_server;
        try {
			dos = new  ObjectOutputStream(user.getOutputStream());
		    dis = new ObjectInputStream(user.getInputStream());
		    dos.reset();
            dos.writeObject(new Message(Message.MsgType.Enter_Name));
            
            
            User_Message um = (User_Message)dis.readObject();
            user_identification = new User(um.user); 
            user_identification.is_online = true;	
            user_identification.ip = user.getInetAddress();
            chat_server.socket_map.put(user_identification, this);
            
            System.out.println("user arrived "+user_identification.username);
            chat_server.user_List.add(user_identification);
            System.out.println("I added u to list");
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
  
    }

    @Override
    public void run() {
        try {                                   
            
            while (true) {
           
                                         
               Message request = (Message)dis.readObject();
                System.out.println("I got a new request");
                
              switch (request.msg) {
              case Bye:
              {
            	  user_identification.is_online=false;
            	  break;
              }
              case List_Users:
              {
                  System.out.println("here i'm writing user list size:"+chat_server.user_List.size());
                  dos.reset();
            	  dos.writeObject(new ListofUseres(chat_server.user_List));
//            	  for (User user : chat_server.user_List) {
//            		  System.out.print(user.username+", ");	
//				}
            	 
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
            		  System.out.println("here i'm writing group list size : "+chat_server.available_groups_list.size());
            		  dos.reset();
            		  dos.writeObject(new ListofGroups(chat_server.available_groups_list));
                       break;
             }
              case Join_Group:
            	   chat_server.join(request.data,user_identification);
            	  break;

              case group_chat_message:
            	  broadcast_messsage_send sent_message = (broadcast_messsage_send)dis.readObject();
            	  sent_message.sender_name=user_identification.username;
            	  new BroadcastThread(sent_message,chat_server,user_identification).start();
            	  break;
			default:
				break;
			}
                

            }
            //Close/release resources
//            dis.close();
//            dos.close();
//            user.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

public class Peer_chat_server {
	List<User> user_List =Collections.synchronizedList(new ArrayList<User>()) ;
	List<available_groups> available_groups_list=Collections.synchronizedList(new ArrayList<available_groups>());
	//List<clientHandler> threadlist=Collections.synchronizedList(new ArrayList<clientHandler>());
	Map<User, clientHandler> socket_map = Collections.synchronizedMap(new HashMap<User, clientHandler>());
 
    public void start() {

        // TODO code application logic here
        try {
            
            ServerSocket sv = new ServerSocket(1234);
            System.out.println("Hello from server");
            while (true) {
                

                Socket peer= sv.accept();
                System.out.println("New peer Arrived");
                clientHandler user_thread = new clientHandler(peer,this);
              //  threadlist.add(user_thread);
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



class available_groups implements Serializable 
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
	User sender;
	Peer_chat_server chat_server;
	 ObjectOutputStream dos;
	public BroadcastThread(broadcast_messsage_send sent_message,
			Peer_chat_server chat_server, User user_identification) {
		this.messag_text=sent_message.data;
		this.sender_name = sent_message.sender_name;
		sender=user_identification;
		this.chat_server=chat_server;
		
		for (available_groups g : chat_server.available_groups_list) {
			if(g.name == sent_message.group_name);
			group=g;
		}
	}


	public void run()
	{
		 
		for (User u : group.group_useres )
		{
			if(u.is_online && ! u.equals(sender) )
			{
				try {
					dos=new ObjectOutputStream(chat_server.socket_map.get(u).user.getOutputStream());
					dos.reset();
					dos.writeObject(new broadcast_messsage_send(messag_text,group.name,sender_name));
				} catch (IOException e) {
						e.printStackTrace();
				}
			}
		}
	}
	
	
}

