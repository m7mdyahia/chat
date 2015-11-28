package chatapp;

/**
 *
 * @author Amr
 */
public class Message {
    
    public enum MsgType {
        Enter_Name,
        User_Name, //sending username and port
        Create_Group,
        List_Groups,
        Join_Group,
        Conv_Msg
    }
    
    public MsgType msg;
    public String data;
    public online_user ouser ;
    
    public void dataTosend(MsgType m,String d){
        this.msg= m;
        this.data= d;
    }
}
