package im.core.define;

import im.protoc.protocolbuf.Protoc;
import io.netty.channel.Channel;

/**
 * Created by Misnearzhang on 2017/4/12.
 */
public abstract class AbstractParse implements Parse , Runnable{

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
