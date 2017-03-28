package im.protoc;

/**
 * Created by zhanglong on 2017/2/25.
 */
public class SystemMessage {
    private String type;
    private Object data;

    public String getType() {
        return type;
    }

    public SystemMessage setType(String type) {
        this.type = type;
        return this;
    }

    public Object getData() {
        return data;
    }

    public SystemMessage setData(Object data) {
        this.data = data;
        return this;
    }
}
