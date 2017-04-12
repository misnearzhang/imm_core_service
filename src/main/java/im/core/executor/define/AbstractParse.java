package im.core.executor.define;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.netty.channel.Channel;

/**
 * Created by Misnearzhang on 2017/4/12.
 */
public abstract class AbstractParse implements Parse, Runnable {

    private Gson gson = new Gson();
    private String message;
    private Channel channel;

    public AbstractParse(String message, Channel channel) {
        this.message = message;
        this.channel = channel;
    }

    @Override
    public void run() {
        Class clazz = setType();
        if (clazz == null) {
            throw new NullPointerException();
        }
        Object object = null;
        try {
            object = gson.fromJson(message, clazz);
        } catch (JsonSyntaxException es) {
            es.printStackTrace();
        }
        parse(object, channel);
    }

    public abstract void parse(Object message, Channel channel);

    public abstract Class setType();
}
