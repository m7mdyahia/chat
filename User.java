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
	
	public User(String username,InetAddress ip,int port) {
		super();
		this.username = username;
		this.ip = ip;
                this.port = port;
	}	
}
