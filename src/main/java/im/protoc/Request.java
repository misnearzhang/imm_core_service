package im.protoc;

/**接收消息抽象
 * Created by zhanglong on 2017/2/25.
 */
public class Request {
    private String uid;//UUID  128bit
    private String type;// 1,普通用户间消息  2.响应消息  3.系统通知消息
    private String content;//加密正文 // message序列化之后的
    private Long timestamp;
    private String sign;//签名校验

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
