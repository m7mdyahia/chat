package chatapp;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
            DataOutputStream dos = new DataOutputStream(user.getOutputStream());
            DataInputStream dis = new DataInputStream(user.getInputStream());
            //Perform IO Operations
            while (true) {
                dos.writeUTF("Please Enter your user name");
                String username = dis.readUTF();
                //Checks must be performed
            //Add ip and user name to vector    
                dos.writeUTF("Welcome :" +username);
                user.getInetAddress();
                online_user online_user1 = new online_user(username, user.getInetAddress())
                chat_server.OnlineUserList.add();
             
             
                
              
                //Checks must be performed
               
              
                String request = dis.readUTF();
                //check
                
              switch (request.MsgType) {
              case create_group:
              {
            	  
            	  chat_server.available_groups_list.add(new available_groups(request.data,online_user1))
				;
				break;		
              }
              case 	

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
	ArrayList<online_user> OnlineUserList;
	ArrayList<available_groups> available_groups_list;

    /**
     * @param args the command line arguments
     */
		
    public  void start() {
        // TODO code application logic here
        try {
            //1.Create Server Socket
            ServerSocket sv = new ServerSocket(1234);
            while (true) {
                //2.Listen for Clients
                Socket peer= sv.accept();
                System.out.println("New peer Arrived");
                clientHandler user_thread = new clientHandler(peer,this);
                user_thread.start();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}


class online_user
{
	String username;
	InetAddress ip;
	public online_user(String username, InetAddress ip) {
		super();
		this.username = username;
		this.ip = ip;
	}
	
	
}

class available_groups
{
	String name;
	ArrayList<online_user> group_useres;
	
	
	public available_groups(String name) {
		super();
		this.name = name;
		this.group_useres = new ArrayList<online_user>();
	}
	public available_groups(String name,online_user creator) {
		super();
		this.name = name;
		this.group_useres = new ArrayList<online_user>();
		this.group_useres.add(creator);
	}

	public boolean  broadcast(String msg)
	{
		return false;
	}
	
}

