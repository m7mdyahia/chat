package chatapp;

/**
 *
 * @author Amr
 */
public class Message {
    
    public enum MsgType {
        Create_Group,
        List_Groups,
        Join_Group,
        Conv_Msg
    }
    
    public MsgType msg;
    public String data;
    
    public void dataTosend(MsgType m,String d){
        this.msg= m;
        this.data= d;
    }
}
