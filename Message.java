package chatapp;

import java.util.ArrayList;


public class Message {
    
    public enum MsgType {
        Enter_Name,
        User_Name, //sending username and port
        Create_Group,
        List_Groups,
        Join_Group,
        Conv_Msg,
        List_Users
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
	
	public ListofUseres(String data,ArrayList<User> userlist) {
		super( MsgType.List_Users, data);
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

	ArrayList<available_groups> grouplist;
}