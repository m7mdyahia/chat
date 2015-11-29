package chatapp;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class User implements Serializable
{
	String username;
	InetAddress ip;
	int port;
	boolean is_online;
	Socket socket;
	
	
	public User(User user) {
		super();
		this.username = user.username;
		this.ip = user.ip;
		this.port = user.port;
		this.is_online = user.is_online;
	}

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

	public User(String username, InetAddress ip, int port, boolean is_online,
			Socket socket) {
		super();
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.is_online = is_online;
		this.socket = socket;
	}	
}
