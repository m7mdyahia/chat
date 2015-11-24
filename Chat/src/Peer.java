package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class clientHandler extends Thread {

    private Socket client;

    // constructor
    public clientHandler(Socket client) {
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
                dos.writeUTF("Please Enter your Account Number");
                String AN = dis.readUTF();
                //Checks must be performed
                dos.writeUTF(AN + " is Valid \nPlease Enter your Password");
                String pass = dis.readUTF();
                //Checks must be performed
                dos.writeUTF("Correct Password \nPlease Enter your Payment Amount");
                String amount = dis.readUTF();
                //Checks must be performed
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
            client.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Peer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.print("hi");
        
        // TODO code application logic here
        try {   
            ServerSocket sv = new ServerSocket(1234);
            //2.Listen for Clients
            Socket c;
            c = sv.accept();
            clientHandler ch = new clientHandler(c);
            ch.start();
                
            //1.Create Client Socket and connect to the server
            Socket client = new Socket("127.0.0.1", 1234);
            //2.if accepted create IO streams
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            DataInputStream dis = new DataInputStream(client.getInputStream());
            
            Scanner sc = new Scanner(System.in);
            String userInput;
            //3.Perform IO operations
            while (true) {
                String response;
                //read the response from the server
                response = dis.readUTF();
                //Print response
                System.out.println(response);
                if (response.equalsIgnoreCase("Bye")) {
                    break;
                }
                //read from the user
                userInput = sc.nextLine();
                dos.writeUTF(userInput);
                
                Message m1= new Message();
                m1.dataTosend(Message.MsgType.Create_Group, "Group1");
                
                System.out.println(m1.data);
                System.out.println(m1.msg);
               
                
            }
            //4.Close /release resources
            dis.close();
            dos.close();
            client.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}