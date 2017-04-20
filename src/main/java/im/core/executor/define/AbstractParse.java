package im.core.executor.define;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import im.core.container.Container;
import im.core.exception.NotOnlineException;
import im.protoc.MessageEnum;
import im.utils.CommUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.nio.charset.Charset;

/**
 * Created by Misnearzhang on 2017/4/12.
 */
public abstract class AbstractParse implements Parse , Runnable{

    private Gson gson = new Gson();
    private String message;
    private Channel channel;

    @Override
    public void run() {
        Class clazz = setType();
        if (clazz == null) {
            throw new NullPointerException();
        }
        Object object = null;
        try {
            object = gson.fromJson(message, clazz);
            parse(object , channel);
        } catch (Exception es) {
            channel.writeAndFlush(Unpooled.copiedBuffer(CommUtil.createErrorResponse(), Charset.defaultCharset()));
            es.printStackTrace();
        }
    }
    public abstract void parse(Object object , Channel channel);

    public abstract Class setType();

    public void initData(String message, Channel channel){
        this.message = message;
        this.channel = channel;
    }
}
