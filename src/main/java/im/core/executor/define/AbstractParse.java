package im.core.executor.define;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import im.core.container.Container;
import im.core.exception.NotOnlineException;
import im.protoc.MessageEnum;
import im.protoc.protocolbuf.Protoc;
import im.utils.CommUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.nio.charset.Charset;

/**
 * Created by Misnearzhang on 2017/4/12.
 */
public abstract class AbstractParse implements Parse , Runnable{

    private Gson gson = new Gson();
    private Protoc.Message message;
    private Channel channel;

    @Override
    public void run() {
        parse(message , channel);
    }
    public abstract void parse(Protoc.Message object , Channel channel);

    public void initData(Protoc.Message message, Channel channel){
        this.message = message;
        this.channel = channel;
    }
}
