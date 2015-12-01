package chatapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable{
    
    public enum MsgType {
        Enter_Name,//sever asks client for identification
        User_Name, //client reply  user name and port
        Create_Group,//peer sends a request of new group 
        List_Groups, //list available groups
        Join_Group,
        Conv_Msg,
        Group_Msg,
        List_Users, //list online users
        Bye,
        group_chat_message,
        
        
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
	
	public ListofUseres(List<User> user_List) {
		super( MsgType.List_Users);
		this.userlist = user_List;
	}

	List<User> userlist;
}

class ListofGroups extends Message
{
	
	public ListofGroups(String data,ArrayList<available_groups> grouplist) {
		super( MsgType.List_Groups, data);
		this.grouplist = grouplist;
	}
	public ListofGroups(List<available_groups> available_groups_list) {
		super( MsgType.List_Groups);
		this.grouplist = available_groups_list;
	}

	List<available_groups> grouplist;
}
class broadcast_messsage_send  extends Message
{
	String group_name;
	String sender_name;
	public broadcast_messsage_send(String resala_nafsha,String group_name) {
		super(MsgType.group_chat_message, resala_nafsha);
		this.group_name=group_name;
		
	}
	public broadcast_messsage_send(String resala_nafsha,String group_name,String sender_name) {
		super(MsgType.group_chat_message, resala_nafsha);
		this.group_name=group_name;
		this.sender_name=sender_name;
		
	}
	
}