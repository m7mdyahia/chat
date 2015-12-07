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
				{     Available_group g=  new Available_group(request.data,user_identification);
				chat_server.available_groups_list.add(g)
				;
				System.out.println("now group "+g );
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

					broadcast_messsage_send sent_message = (broadcast_messsage_send)request;
					System.out.println("Recived Group message request");
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
	List<Available_group> available_groups_list=Collections.synchronizedList(new ArrayList<Available_group>());
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




	public void join(String data,User user) {


		for (Available_group available_group : available_groups_list) 
		{
			if(available_group.name.equals(data))
			{
				available_group.group_useres.add(user);
				System.out.println("user "+ user.username + "added to "+available_group.name );
			}
		}

	}
}


class Available_group implements Serializable 
{
	String name;
	ArrayList<User> group_useres;


	@Override
	public String toString() {
		return "Available_group [name=" + name + ", group_useres="
				+ group_useres.toString() + "size : ="+group_useres.size()+"]";
	}
	public Available_group(String name) {
		super();
		this.name = name;
		this.group_useres = new ArrayList<User>();
	}
	public Available_group(String name,User creator) {
		super();
		this.name = name;
		this.group_useres = new ArrayList<User>();
		this.group_useres.add(creator);
	}


}

class BroadcastThread extends Thread
{
	String messag_text;
	Available_group group;
	String sender_name;
	User sender;
	Peer_chat_server chat_server;
	ObjectOutputStream dos;
	broadcast_messsage_send m;
	public BroadcastThread(broadcast_messsage_send sent_message,
			Peer_chat_server chat_server, User user_identification) {
		this.messag_text=sent_message.data;
		this.sender_name = sent_message.sender_name;
		sender=user_identification;
		this.chat_server=chat_server;

		for (Available_group g : chat_server.available_groups_list) {
			if(g.name == sent_message.group_name);
			group=g;
		}
		m=new broadcast_messsage_send(messag_text,group.name,sender_name);
		System.out.println("beoadcasting " +m.toString());
	}


	@Override
	public void run()
	{
		System.out.println("begin broadcast to "+ group.name+" "+"containing "+group.group_useres.size());

		for (User u : group.group_useres )
		{
			System.out.println("u: " +u.username+" "+u.is_online);
			System.out.println("sender.username= "+sender.username);
			if(u.is_online && ! u.username.equals(sender.username) )
			{
				try 
				{	System.out.println("sending to" +chat_server.socket_map.get(u).user_identification.username );
				dos=chat_server.socket_map.get(u).dos;
				dos.reset();
				dos.writeObject(m);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


}

