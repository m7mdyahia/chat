package chatapp;

import java.net.InetAddress;

public class User
{
	String username;
	InetAddress ip;
	int port;
	bool is_online;
	public User(String username, InetAddress ip) {
		super();
		this.username = username;
		this.ip = ip;
	}
	
}
