package chatapp;

import java.util.Scanner;

public class user_interface {
    public static void main(String[] args) 
    {
            // TODO Auto-generated method stub
        Scanner sc = new Scanner(System.in);
        int userInput;
        Peer p;
        userInput= sc.nextInt();
        switch (userInput)
        {
            case 1:
                new Peer_chat_server().Start();
                break;
            case 2:
                p = new Peer();
                p.start();
                while(true)
                {
                    userInput= sc.nextInt();
                    switch (userInput)
                    {
                        case 1:
                            p.exit();
                        
                    }
                }
            default:
                break;
        }
    }
}
