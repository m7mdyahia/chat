package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.objects.NativeArray;

class serverConnection extends Thread {
    
    Socket client; //socket connecting to server
    Peer p;
    ObjectOutputStream dos;
    ObjectInputStream dis;
    
    public serverConnection (Peer p) { this.p = p; }
    
    public void update_me() 
    { 
        try 
        {
            dos.writeObject(new Message(Message.MsgType.List_Users));
            p.list_of_users=((ListofUseres)dis.readObject()).userlist;
            
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
            dos.writeObject(new Message(Message.MsgType.Bye));
            dis.close();
            dos.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(serverConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
           
            Message m= new Message();
            //read the response from the server
            m = (Message)dis.readObject();
            //Print response
            System.out.println("Please enter your name?");

            if (m.msg == Message.MsgType.Enter_Name) {
                userInput = sc.nextLine();
                dos.writeObject(new User_Message(new User(userInput,p.port)));
            }
            dos.writeObject(new Message(Message.MsgType.Bye));
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
}


class PeerHandler extends Thread {   
    private Socket client;
    // constructor
    public PeerHandler(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("New Client Arrived");
            //Create IO Streams
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            DataInputStream dis = new DataInputStream(client.getInputStream());
            //Perform IO Operations
            while (true) {
                //Checks must be performed
                dos.writeUTF("Hello peer");
                String Choice = dis.readUTF();
                if (Choice.equalsIgnoreCase("No")) {
                    dos.writeUTF("Bye");
                    break;
                }
            }
            //Close/release resources
            dis.close();
            dos.close();
            client.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class PeerConnection extends Thread {
    Peer PP;
    Socket clientP;
    public PeerConnection (Peer p) { this.PP = p; }
    
    @Override
    public void run() {
        try {
            
            clientP = new Socket(PP.peer_callee.ip, PP.peer_callee.port);
            //2.if accepted create IO streams
            dos = new ObjectOutputStream(clientP.getOutputStream());
            dis = new ObjectInputStream(clientP.getInputStream());
            
            System.out.println("New Client Arrived");
            //Create IO Streams
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            DataInputStream dis = new DataInputStream(client.getInputStream());
            //Perform IO Operations
            while (true) {
                //Checks must be performed
                dos.writeUTF("Hello peer");
                String Choice = dis.readUTF();
                if (Choice.equalsIgnoreCase("No")) {
                    dos.writeUTF("Bye");
                    break;
                }
            }
            //Close/release resources
            dis.close();
            dos.close();
            client.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class Peer {

    int port;
	 /**
     * @param args the command line arguments
     */
    serverConnection sc;
    
    public ArrayList<User>  list_of_users;
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
            
            while(true) {            
                Socket c;
                c = sv.accept();
                PeerHandler ph = new PeerHandler(c);
                ph.start();
            }
            
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
}