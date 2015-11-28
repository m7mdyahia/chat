package chatapp;

import java.net.InetAddress;

public class User
{
	String username;
	InetAddress ip;
	int port;
	boolean is_online;
	public User(String username,int port) {
		super();
		this.username = username;
                this.port = port;
	}
	
<<<<<<< HEAD
	public User(String username,InetAddress ip,int port) {
		super();
		this.username = username;
		this.ip = ip;
                this.port = port;
	}	
=======
>>>>>>> 56c624096ba10c81aa6c6ccba763f9d611918d46
}
