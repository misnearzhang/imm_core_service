package im.protoc;

/**
 * Created by zhanglong on 2017/2/25.
 */
public class SystemMessage {
    private String uid;//UUID  128bit
    private String type;//被顶下线等系统消息

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
