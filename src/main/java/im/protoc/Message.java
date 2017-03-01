package im.protoc;

/**接收消息抽象
 * Created by zhanglong on 2017/2/25.
 */
public class Message {
    private String uid;//UUID  128bit
    private String status;//
    private String type;// 1,普通用户间消息  2.响应消息  3.系统通知消息 4.心跳
    private Long timestamp;
    private String body;

    public String getUid() {
        return uid;
    }

    public Message setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getType() {
        return type;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Message setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Message setBody(String body) {
        this.body = body;
        return this;
    }

    public Message(String uid, String status, String type, Long timestamp) {
        this.uid = uid;
        this.status = status;
        this.type = type;
        this.timestamp = timestamp;
    }
}
