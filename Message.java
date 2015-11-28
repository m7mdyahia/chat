package chatapp;

import java.io.Serializable;
import java.util.ArrayList;


public class Message implements Serializable{
    
    public enum MsgType {
        Enter_Name,
        User_Name, //sending username and port
        Create_Group,
        List_Groups, //list available groups
        Join_Group,
        Conv_Msg,
        Group_Msg,
        List_Users, //list online users
        Bye
        
    }
    public Message() {
		super();
    }
    public Message(MsgType msg, String data) {
		super();
		this.msg = msg;
		this.data = data;
	}
    public Message(MsgType msg) {
		super();
		this.msg = msg;
	}
	public MsgType msg;
    public String data;
    public void dataTosend(MsgType m,String d){
        this.msg= m;
        this.data= d;
    }
}

class User_Message extends Message 
{
	
	public User_Message(User user) {
		super( MsgType.User_Name);
		this.user = user;
	}

	User user; 
	
}

class ListofUseres extends Message
{
	
	public ListofUseres(ArrayList<User> userlist) {
		super( MsgType.List_Users);
		this.userlist = userlist;
	}

	ArrayList<User> userlist;
}

class ListofGroups extends Message
{
	
	public ListofGroups(String data,ArrayList<available_groups> grouplist) {
		super( MsgType.List_Groups, data);
		this.grouplist = grouplist;
	}
	public ListofGroups(ArrayList<available_groups> grouplist) {
		super( MsgType.List_Groups);
		this.grouplist = grouplist;
	}

	ArrayList<available_groups> grouplist;
}
class broadcast_messsage_send  extends Message
{
	String group;

	public broadcast_messsage_send(String resala_nafsha,String group_name) {
		super(  MsgType.Conv_Msg, group_name);
		this.group=group;
		// TODO Auto-generated constructor stub
	}
	
}
