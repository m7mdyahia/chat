package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            
            
            Scanner sc = new Scanner(System.in);
            String userInput;
            
            System.out.println("Connection Succesful");
           
            Message m= new Message();
            //read the response from the server
            m = (Message)dis.readObject();
            //Print response
            System.out.println("Please enter your name?");

            if (m.msg == Message.MsgType.Enter_Name) {
                userInput = sc.nextLine();
                dos.writeObject(new User_Message(new User(userInput,p.port)));
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
            System.out.println(client.getInetAddress());
            dos.writeObject(new Message(Message.MsgType.List_Users));
            p.list_of_users=((ListofUseres)dis.readObject()).userlist;
            System.out.println(p.list_of_users.get(0).username);
            
            dos.writeObject(new Message(Message.MsgType.List_Groups));
            p.group_list=((ListofGroups)dis.readObject()).grouplist;
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
            } catch (IOException ex) {
                Logger.getLogger(PeerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class PeerHandler extends Thread {
    
    String receivedMsg;
    String SentMsg;
     Socket client;
    // constructor
    public PeerHandler(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("New Friend Arrived");
            
            //Perform IO Operations
            PeerHandlerReader phr = new PeerHandlerReader(this);
            phr.start();
            
            PeerHandlerSender phw = new PeerHandlerSender(this);
            phw.start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class PeerHandlerReader extends Thread 
{
    PeerHandler ph;
    ObjectInputStream dis;
    public PeerHandlerReader (PeerHandler p) { this.ph = p; }
    
    @Override
    public void run() 
    {
        
        try {
            while (true)
            {
                try {
                    dis = new ObjectInputStream(ph.client.getInputStream());
                    
                    try {
                        if(((Message)dis.readObject()).msg == Message.MsgType.Conv_Msg)
                        {
                            ph.receivedMsg = ((Message)dis.readObject()).data;
                            System.out.println(ph.receivedMsg);
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PeerHandlerReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    try {
                        if(((Message)dis.readObject()).msg == Message.MsgType.Bye)
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
            dis.close();
            ph.client.close();
        } catch (IOException ex) {
            Logger.getLogger(PeerHandlerReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class PeerHandlerSender extends Thread 
{
    PeerHandler ph;
    ObjectOutputStream dos;
    public PeerHandlerSender (PeerHandler p) { this.ph = p; }
    Scanner sc = new Scanner(System.in);
    
    @Override
    public void run() 
    {
        while (true)
        {
            try {
                ph.SentMsg = sc.nextLine();
                dos.writeObject(new Message(Message.MsgType.Conv_Msg,ph.SentMsg));
                System.out.println(ph.SentMsg);
                
            } catch (IOException ex) {
                Logger.getLogger(PeerConnectionSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

class PeerConnection extends Thread {
    String receivedMsg;
    String SentMsg;
    Peer PP;
    Socket clientP;
    public PeerConnection (Peer p) { this.PP = p; }
    ObjectOutputStream dos;
    
    
    @Override
    public void run() {
        try {
            
            clientP = new Socket(PP.peer_callee.ip, PP.peer_callee.port);
            //2.if accepted create IO streams
            
            PeerConnectionReader pcr = new PeerConnectionReader(this);
            pcr.start();
            
            PeerConnectionSender pcs = new PeerConnectionSender(this);
            pcs.start();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class PeerConnectionReader extends Thread 
{
    PeerConnection pc;
    ObjectInputStream dis;
    public PeerConnectionReader (PeerConnection p) { this.pc = p; }
    
    @Override
    public void run() 
    {
        try {
            while (true)
            {
                try {
                    dis = new ObjectInputStream(pc.clientP.getInputStream());
                    
                    try {
                        if(((Message)dis.readObject()).msg == Message.MsgType.Conv_Msg)
                        {
                            pc.receivedMsg = ((Message)dis.readObject()).data;
                            System.out.println(pc.receivedMsg);
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PeerConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        if(((Message)dis.readObject()).msg == Message.MsgType.Bye)
                        {
                            break;
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PeerConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PeerConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            dis.close();
            pc.clientP.close();
        } catch (IOException ex) {
            Logger.getLogger(PeerConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class PeerConnectionSender extends Thread 
{
    PeerConnection pc;
    ObjectOutputStream dos;
    public PeerConnectionSender (PeerConnection p) { this.pc = p; }
    Scanner sc = new Scanner(System.in);
    
    @Override
    public void run() 
    {
        while (true)
        {
            try {
                pc.SentMsg = sc.nextLine();
                dos.writeObject(new Message(Message.MsgType.Conv_Msg,pc.SentMsg));
                System.out.println(pc.SentMsg);
                
            } catch (IOException ex) {
                Logger.getLogger(PeerConnectionSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

class Peer {

    int port;
	 /**
     * @param args the command line arguments
     */
    serverConnection sc;
    
    public List<User>  list_of_users;
    public ArrayList<available_groups> group_list;
    public User peer_callee;
    
    public void exit() { sc.exit(); }
    public void update_me() { sc.update_me(); }
    
    public void call_peer(String name) { 
        for (User user : list_of_users){
            if(user.username == name)
                peer_callee = user;
        }
        PeerConnection pc = new PeerConnection(this);
        pc.start();
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
            
//            PeerListener pl = new PeerListener(sv);
//            pl.start();   
        } 
        catch (Exception e) 
        {
          System.out.println(e.getMessage());
        }
    }
}