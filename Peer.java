package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class serverConnection extends Thread {
    
    Peer p;
    
    public serverConnection (Peer p)
    {
        this.p = p;
    }
    
    @Override
    public void run() { 
        try 
        {
            //1.Create Client Socket and connect to the server
            Socket client = new Socket("127.0.0.1", 1234);
            //2.if accepted create IO streams
            ObjectOutputStream dos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream dis = new ObjectInputStream(client.getInputStream());
            
            Scanner sc = new Scanner(System.in);
            String userInput;
            //3.Perform IO operations
            while (true) {
            
                Message m= new Message();
                //read the response from the server
                m = (Message)dis.readObject();
                //Print response
                System.out.println(m.data);
                
                if (m.msg == Message.MsgType.Enter_Name) {
                    userInput = sc.nextLine();
                    dos.writeObject(new User_Message(new User(userInput,p.port)));
                }
                
                dos.writeObject(new Message(Message.MsgType.Bye));
                
                if (m.msg == Message.MsgType.Bye) {
                    break;
                }
            }
            dis.close();
            dos.close();
            client.close();
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

class Peer {

    int port;
	 /**
     * @param args the command line arguments
     */
    public void start() { 
        // TODO code application logic here
        try {
            ServerSocket sv = new ServerSocket(0);
            port = sv.getLocalPort();

            serverConnection sc = new serverConnection(this);
            sc.start();
            
            while(true) {            
                Socket c;
                c = sv.accept();
                PeerHandler ph = new PeerHandler(c);
                ph.start();
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}