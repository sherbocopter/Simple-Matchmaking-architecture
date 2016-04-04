package networkUtils;

import java.io.Serializable;

/**
 *
 * @author SHerbocopter
 */
public class Message implements Serializable {
    public enum MsgType {
        MSG_INVALID,
        REQ_LOGIN,          RES_LOGIN,
        REQ_MM,             RES_MM,
        MSG_TEXT, //data field is null
        MSG_DISCONNECT //not necessary
    };
    
    public MsgType type;
    public SerData data;
    public String text;
    
    public Message() {
        type = MsgType.MSG_INVALID;
        data = null;
        text = "";
    }
}
