package chatapp;

import java.util.Scanner;

public class user_interface {
    public static void main(String[] args) 
    {
            // TODO Auto-generated method stub
        Scanner sc = new Scanner(System.in);
        int userInput2=4;
        
        int userInput;
        
        Peer p;
        
        userInput= sc.nextInt();
        
        switch (userInput)
        {
            case 1:
                new Peer_chat_server().start();
                break;
            case 2:
               // p = new Peer();
               // p.start();
//                while(true)
//                {
                	System.out.println("Enter your choice:");
                	if(sc.hasNextInt())
                	{
                	System.out.println("we considered this int");
                     userInput2= sc.nextInt();
                    
                	}
                    switch (userInput2)
                    {
                        case 1:
                          //  p.update_me();
                      //      break;
                  //     case 2:
                            System.out.println("Enter peer name:");
                            Scanner sm = new Scanner(System.in);
                            String nameInput;
                            nameInput = sm.nextLine();
                        //    p.call_peer(nameInput);
                        case 3:
                          //  p.exit();
                        case 4:
                        	sc.close();
                    }
//                }
            default:
                break;
        }
   System.out.println("exiting main of interface");
   }
}
