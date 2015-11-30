package chatapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates a thread which handles connection to server
 * @author Amr Ragaey
 *
 */
class serverConnection extends Thread {
    
    Socket client; //socket connecting to server
    Peer p;
    public ObjectOutputStream dos;
    public ObjectInputStream dis; 

    public serverConnection (Peer p) { this.p = p; }
    
    @Override
    public void run() 
    { 
        try 
        {
            //1.Create Client Socket and connect to the server
            client = new Socket("127.0.0.1", 1234);

            //2.if accepted create IO streams
            dos = new ObjectOutputStream(client.getOutputStream());
            dis = new ObjectInputStream(client.getInputStream());
            
            
         //   Scanner sc = new Scanner(System.in);
          // String userInput;
            
            System.out.println("Connection Succesful");
           
            Message m= new Message();
            //read the response from the server
            m = (Message)dis.readObject();
            //Print response
            System.out.println("Please enter your name?");

            if (m.msg == Message.MsgType.Enter_Name) {
               // userInput = sc.nextLine();
                dos.writeObject(new User_Message(new User(p.username,p.port)));
            }
//            dos.writeObject(new Message(Message.MsgType.Bye));
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void update_me() 
    { 
        try 
        {
            dos.writeObject(new Message(Message.MsgType.List_Users));
            p.list_of_users=((ListofUseres)dis.readObject()).userlist;
            for (User x : p.list_of_users) {
            	System.out.print(x.username + ", ");
            }
            

        } catch (IOException ex) {
            Logger.getLogger(serverConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(serverConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 
    public void exit()
    {
        try 
        {
            dos = new ObjectOutputStream(client.getOutputStream());
            
            dos.writeObject(new Message(Message.MsgType.Bye));
            dis.close();
            dos.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(serverConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/**
 *	this class waits on the server socket for other peer to connect then creates 
 *	a pear handler thread
 * @author Amr Ragay
 *
 *	 */
class PeerListener extends Thread 
{
    ServerSocket sv;
    public PeerListener (ServerSocket s) {
        this.sv = s;
    }
    @Override
    public void run()
    {
        while(true) 
        {
            try {
                Socket c;
                c = sv.accept();
                PeerHandler ph = new PeerHandler(c);
                ph.start();
                try {
					ph.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } catch (IOException ex) {
                Logger.getLogger(PeerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class PeerHandler extends Thread {
    
    
     Socket client;
     ObjectInputStream dis;
     ObjectOutputStream dos;
    // constructor
    public PeerHandler(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("New Friend Arrived");             
            
            dos = new ObjectOutputStream(client.getOutputStream());
            PeerHandlerSender phw = new PeerHandlerSender(dos);
            phw.start();            
            
            dis = new ObjectInputStream(client.getInputStream());
            PeerHandlerReader phr = new PeerHandlerReader(dis);
            phr.start();
            phr.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class PeerHandlerReader extends Thread 
{
	Message receivedMsg;
   // PeerHandler ph;
	ObjectInputStream dis;
   // public PeerHandlerReader (PeerHandler p) { this.ph = p;}
    
    public PeerHandlerReader(ObjectInputStream dis) {
		this.dis=dis;
		}

	@Override
    public void run() 
    {   
        while (true)
		{
		    try {
		       
		        
		        try {
		        	receivedMsg=((Message)dis.readObject());
		        //	System.out.println("we recived a new message");
		        	
		            if(receivedMsg.msg.equals(Message.MsgType.Conv_Msg))
		            {
		            //	System.out.println("we recived a new conv message");
		               // System.out.println("A new message arrived");
		                System.out.println(receivedMsg.data);
		            }
		        } catch (ClassNotFoundException ex) {
		            Logger.getLogger(PeerHandlerReader.class.getName()).log(Level.SEVERE, null, ex);
		        }
		        
		        try {
		            if(((Message)dis.readObject()).msg.equals(Message.MsgType.Bye))
		            {
		                break;
		            }
		        } catch (ClassNotFoundException ex) {
		            Logger.getLogger(PeerHandlerReader.class.getName()).log(Level.SEVERE, null, ex);
		        }
		    } catch (IOException ex) {
		        Logger.getLogger(PeerHandlerReader.class.getName()).log(Level.SEVERE, null, ex);
		    }

		}
        //  ph.dis.close();
        //  ph.client.close();
    }
}

class PeerHandlerSender extends Thread 
{
	String SentMsg;
    
	ObjectOutputStream dos;
   
    public PeerHandlerSender(ObjectOutputStream dos) {
		this.dos=dos;
	}
	Scanner sc = new Scanner(System.in);
    
    @Override
    public void run() 
    {
        while (true)
        {    
        	while(sc.hasNextLine()) {
        	   
        	
               SentMsg = sc.nextLine();
               System.out.println("tring to send" + SentMsg);
        	
                try {
					dos.writeObject(new Message(Message.MsgType.Conv_Msg,SentMsg));
				//	System.out.println("we sent"+SentMsg);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
             
           
        }
        }
    }
}

/**
 * This class creates a thread in which a peer connects to other peer
 * @author Amr Ragaey
 *
 */
class PeerConnection extends Thread {
    
    /**
     * This is the peer object for the calee peer passed when constructing the class
     */
    Peer PP;
    Socket clientP;
    ObjectInputStream dis;
    ObjectOutputStream dos;
    public PeerConnection (Peer p) { this.PP = p; }
    
    
    @Override
    public void run() {
        try {
            
            clientP = new Socket(PP.peer_callee.ip, PP.peer_callee.port);
            //2.if accepted create IO streams
            dis = new ObjectInputStream(clientP.getInputStream());
            PeerHandlerReader pcr = new PeerHandlerReader(dis);
            pcr.start();
            
            dos = new ObjectOutputStream(clientP.getOutputStream());
            PeerHandlerSender pcs = new PeerHandlerSender(dos);
            pcs.start();
            pcs.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}



class Peer {

    int port;
    String username;
    
	 public Peer(String username) {
		super();
		this.username = username;
	}

	/**
     * @param args the command line arguments
     */
    serverConnection sc;
    
    public List<User>  list_of_users;
    public List<available_groups> group_list;
    public List<Calee_user> PeerConnectionList= Collections.synchronizedList(new ArrayList<Calee_user>());
    public User peer_callee;
    
    public void exit() { sc.exit(); }
    public void update_me() { sc.update_me(); }
    public void send(String msg,String username)
    {
    	for (Calee_user calee_user : PeerConnectionList) {
			if(calee_user.username.equals(username))
				try {
					calee_user.peerconnection.dos.writeObject(new Message(Message.MsgType.Conv_Msg, msg));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
    }
    
    public void call_peer(String name) { 
        for (User user : list_of_users){
            if(user.username.equalsIgnoreCase(name)){
                peer_callee = user;}
        }
        System.out.println("Your Peer is:" + peer_callee.username);
        PeerConnection pc = new PeerConnection(this);
        pc.start();
        PeerConnectionList.add(new Calee_user(peer_callee, pc));
        try {
			pc.join();// peer will wait until p2p ends
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void start() 
    {
        try 
        {
            ServerSocket sv = new ServerSocket(0);
            port = sv.getLocalPort();
            
            sc = new serverConnection(this);
            sc.start();
            sc.join();
            
            PeerListener pl = new PeerListener(sv);
            pl.start();   
        } 
        catch (Exception e) 
        {
          System.out.println(e.getMessage());
        }
    }
	public void creategroup(String groupname) {
		try {
			sc.dos.writeObject(new Message(Message.MsgType.Create_Group,groupname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void joingroup(String group_name) {
		try {
			sc.dos.writeObject(new Message(Message.MsgType.Join_Group,group_name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void send_group_message(String gropname, String message) {
		try {
			sc.dos.writeObject(new broadcast_messsage_send(message,gropname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}