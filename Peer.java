package chatapp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chatapp.Message.MsgType;


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
    Message m;
    public serverConnection (Peer p) { 
    	this.p = p; 
    
    try {
    	client = new Socket("127.0.0.1", 1234);      
        dos = new ObjectOutputStream(client.getOutputStream());
		dis = new ObjectInputStream(client.getInputStream());	  
		System.out.println("Connection Succesful");
        m = (Message)dis.readObject();   //read the response from the server
        if(m.msg==MsgType.Enter_Name)
        {	
        System.out.println("recived authentication request");
		dos.reset();
		dos.writeObject(new User_Message(new User(p.username,p.port)));
		System.out.println("Authentication Succesful");
        }
	} catch (IOException | ClassNotFoundException e) {
	
		e.printStackTrace();
	}
      
    
    }
    
    @Override
    public void run() 
    { 
        try 
        {
                      
           while(true){          
        	   m = (Message)dis.readObject();
            switch (m.msg) {
            case List_Users:
            	System.out.println("Recived user list size: "+(((ListofUseres)m).userlist).size());
            	p.list_of_users= Collections.synchronizedList(new ArrayList<User>(((ListofUseres)m).userlist));
            	p.main.update_list_of_users();
            	break;
            case List_Groups:
            	System.out.println("Recived group list");
            	  p.group_list=Collections.synchronizedList(new ArrayList<Available_group>( ((ListofGroups)m).grouplist));
            	  break;
            case group_chat_message:
				p.recived_group_message((broadcast_messsage_send)m);
				break;
			default:
				break;
			            	
            }
           }
        }
        catch (Exception e) 
        {
        	
            System.out.println(e.getMessage());
        }
    }
    
    public  void update_me() 
    { 
        try 
        {
        	dos.reset();
            dos.writeObject(new Message(Message.MsgType.List_Users));
            System.out.println("i sent user update request ");          
          //  p.list_of_users= Collections.synchronizedList(new ArrayList<User>(((ListofUseres)dis.readObject()).userlist));
        
            dos.reset();   
            dos.writeObject(new Message(Message.MsgType.List_Groups));
            System.out.println("i sent group update request ");   
       //     p.group_list=Collections.synchronizedList(new ArrayList<available_groups>( ((ListofGroups)dis.readObject()).grouplist));
        } catch (IOException ex) {
            Logger.getLogger(serverConnection.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
 
    public void exit()
    {
        try 
        {
            dos = new ObjectOutputStream(client.getOutputStream());
            dos.reset();
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
    PeerHandler ph;
    Peer peer;
    public PeerListener (ServerSocket s, Peer peer) {
        this.sv = s;
        this.peer = peer ;
        }
    @Override
    public void run()
    {
        while(true) 
        {
            try {
                Socket c;
                c = sv.accept();
                 ph = new PeerHandler(this,c,peer);
                ph.start();
                peer.ph=ph;//TODO must be add to list
                try {
					ph.join();//TODO review
				} catch (InterruptedException e) {
					
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
     Peer peer;
     PeerListener pl ;
     PeerHandlerSender phw;
    // constructor
    public PeerHandler(PeerListener pl,Socket client,Peer p) {
        this.client = client;
        this.pl = pl ;
        this.peer =p;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("New Friend Arrived");             
            
            dos = new ObjectOutputStream(client.getOutputStream());
             phw = new PeerHandlerSender(dos,peer);
          //  phw.start();            
            
            dis = new ObjectInputStream(client.getInputStream());
            PeerHandlerReader phr = new PeerHandlerReader(this,peer);
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
   
	ObjectInputStream dis;
	PeerConnection pc ;
	PeerHandler ph;
	Peer peer;
   // public PeerHandlerReader (PeerHandler p) { this.ph = p;}
    
    public PeerHandlerReader(PeerHandler  ph,Peer pp) {
		this.ph=ph;
		this.peer=pp;
		this.dis=ph.dis;
		}

	public PeerHandlerReader(PeerConnection peerConnection, Peer pp) {
	this.pc = peerConnection;
	this.peer=pp;
	this.dis=peerConnection.dis;
		
}

	@Override
    public void run() 
    {   
        while (true)
		{
		    try {
		       
		        
		        try {
	                System.out.println("i enterd the while loop");

		        	receivedMsg=((Message)dis.readObject());
		        	
		        	System.out.println("we recived a new message: " + receivedMsg.data);
		        	peer.main.update_message_box(receivedMsg.data);
		            if(receivedMsg.msg.equals(Message.MsgType.Conv_Msg))
		            {

		                System.out.println(receivedMsg.data);
		            }
		        } catch (ClassNotFoundException ex) {
		            Logger.getLogger(PeerHandlerReader.class.getName()).log(Level.SEVERE, null, ex);
		        }
		        
		        if(receivedMsg.msg.equals(Message.MsgType.Bye))
				{
				    break;
				}
		    } catch (IOException ex) {
		        Logger.getLogger(PeerHandlerReader.class.getName()).log(Level.SEVERE, null, ex);
		    }

		}
        //  ph.dis.close();
        //  ph.client.close();
    }
}

class PeerHandlerSender //extends Thread 
{
	String SentMsg;
    Peer peer;
	ObjectOutputStream dos;
   // public PeerHandlerSender (PeerHandler p) { this.ph = p; }
    public PeerHandlerSender(ObjectOutputStream dos, Peer peer) {
		this.dos=dos;
		this.peer=peer;
	}
    void send(String SentMsg){
    	
		try {
			dos.reset();
			dos.writeObject(new Message(Message.MsgType.Conv_Msg,SentMsg));
			System.out.println("we sent"+SentMsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    PeerHandlerReader phr ;
    PeerHandlerSender pcs;
    public PeerConnection (Peer p) { this.PP = p; }
    public  void send_message(String msg) {
		pcs.send(msg);
		
	}
    
    @Override
    public void run() {
        try {
            
            clientP = new Socket(PP.peer_callee.ip, PP.peer_callee.port);
            //2.if accepted create IO streams
            dis = new ObjectInputStream(clientP.getInputStream());
             phr = new PeerHandlerReader(this,PP);
            phr.start();
            
            dos = new ObjectOutputStream(clientP.getOutputStream());
             pcs = new PeerHandlerSender(dos, PP);

            //  pcs.start();
         //   pcs.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


	
}


class Peer {

    int port;
    String username;
	 /**
     * @param args the command line arguments
     */
    serverConnection sc;
    PeerConnection pc;//TODO convert to list
    PeerListener pl;
    PeerHandler ph;//TODO convert to list
    Main main;//TODO convert to list
    Group group;//TODO convert to list
    
    public Peer(String s, Main main){
    	this.username=s;
    	this.main=main;
    }
    public void recived_group_message(broadcast_messsage_send group_message) {
		group.recived_group_message(group_message);
		
	}
	public List<User>  list_of_users=Collections.synchronizedList(new ArrayList<User>()) ;
    public List<Available_group> group_list=Collections.synchronizedList(new ArrayList<Available_group>());;
  
    public User peer_callee;
    
    public void exit() { sc.exit(); }
    public void update_me() { sc.update_me(); }
    public void send_message_peer(String msg)
    {
    	if(pc!=null)//I initiated connection
    		pc.send_message(msg);
    	else if(ph!=null)//I received the connection
    		ph.phw.send(msg);
    		
    }
    public void call_peer(String name) { 
        for (User user : list_of_users){
            if(user.username.equalsIgnoreCase(name)){
                peer_callee = user;}
        }
        System.out.println("Your Peer is:" + peer_callee.username);
        pc = new PeerConnection(this);
        pc.start();
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
          
            
             pl = new PeerListener(sv,this);
            pl.start();   
        } 
        catch (Exception e) 
        {
          System.out.println(e.getMessage());
        }
    }
	public void creategroup(String groupname) {
		try {
			sc.dos.reset();
			sc.dos.writeObject(new Message(Message.MsgType.Create_Group,groupname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void joingroup(String group_name) {
		try {
			sc.dos.reset();
			sc.dos.writeObject(new Message(Message.MsgType.Join_Group,group_name));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void send_group_message(String gropname, String message) {
		try {
			sc.dos.reset();
			sc.dos.writeObject(new broadcast_messsage_send(message,gropname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}