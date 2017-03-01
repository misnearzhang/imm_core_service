package im.protoc;

import java.io.Serializable;

/**接收消息抽象
 * Created by zhanglong on 2017/2/25.
 */
public class Message implements Serializable{
    private String head;//Header
    private String body;//Body

    public String getHead() {
        return head;
    }

    public Message setHead(String head) {
        this.head = head;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Message setBody(String body) {
        this.body = body;
        return this;
    }
}
