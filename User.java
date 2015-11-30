package chatapp;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable
{
	String username;
	InetAddress ip;
	int port;
	boolean is_online;
	//Socket socket;
	
	
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

	public User(String username, InetAddress ip, int port, boolean is_online) {
		super();
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.is_online = is_online;
		//this.socket = socket;
	}	
}

class Calee_user extends User 
{
	PeerConnection peerconnection;

	public Calee_user(String username, InetAddress ip, int port,
			boolean is_online,PeerConnection peerconnection) {
		super(username, ip, port, is_online);
		this.peerconnection=peerconnection;
		}
	public Calee_user(User u,PeerConnection peerconnection)
	{
		super(u);
		this.peerconnection=peerconnection;
	}
	
}
