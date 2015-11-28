package chatapp;

import java.util.Scanner;

public class user_interface {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Scanner sc = new Scanner(System.in);
         int userInput;
         userInput= sc.nextInt();
         switch (userInput) {
		case 1:
			new Peer_chat_server().start();
			break;
		case 2:
			new Peer().start();
			break;
		default:
			break;
		}
		
		

	}

}
