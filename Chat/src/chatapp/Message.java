package chatapp;

import java.util.ArrayList;

/**
 *
 * @author Mohammad Yahia
 */
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
	public MsgType msg;
    public String data;
    public void dataTosend(MsgType m,String d){
        this.msg= m;
        this.data= d;
    }
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
