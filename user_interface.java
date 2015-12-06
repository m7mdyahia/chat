package chatapp;

import java.util.Scanner;

public class user_interface {
    public static void main(String[] args) 
    {

    	Scanner sc= new Scanner(System.in);
    	String msg;
    	int choice= sc.nextInt();
    	switch (choice) {
		case 1://server
			Peer_chat_server server = new Peer_chat_server();
			
			break;
		case 2:
		//	Peer p1 = new Peer("Amr");
		//	p1.creategroup("CSE2014");
			while(true)
			{
				msg=sc.nextLine();
		//		p1.send_group_message("CSE2014","Testing the group");
			}
			
		case 3:
	//		Peer p2=new Peer("Ali");
	//		p2.joingroup("CSE2014");
	
	break;

		default:
			break;
		}
    	sc.close();
    	
    	
    	
    	
//            // TODO Auto-generated method stub
//        Scanner sc = new Scanner(System.in);
//        int userInput2=4;
//        
//        int userInput;
//        
//        Peer p;
//        
//        userInput= sc.nextInt();
//        
//        switch (userInput)
//        {
//            case 1:
//                new Peer_chat_server().start();
//                break;
//            case 2:
//                p = new Peer("aa");
//                p.start();
////                while(true)
////                {
//                	System.out.println("Enter your choice:");
//                	if(sc.hasNextInt())
//                	{
//                	System.out.println("we considered this int");
//                     userInput2= sc.nextInt();
//                    
//                	}
//                    switch (userInput2)
//                    {
//                        case 1:
//                            p.update_me();
//                      //      break;
//                  //     case 2:
//                            System.out.println("Enter peer name:");
//                            Scanner sm = new Scanner(System.in);
//                            String nameInput;
//                            nameInput = sm.nextLine();
//                            p.call_peer(nameInput);
//                        case 3:
//                            p.exit();
//                        case 4:
//                        	sc.close();
//                    }
////                }
//            default:
//                break;
//        }

   System.out.println("exiting main of interface");
   }
}
