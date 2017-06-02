package im.core.define;

import im.core.server.ThreadPool;
import im.protoc.protocolbuf.Protoc;
import io.netty.channel.Channel;

/**
 * Created by Misnearzhang on 2017/4/12.
 */
public abstract class AbstractParse implements Parse , Runnable{

    private Protoc.Message message;
    private Channel channel;
    public ThreadPool threadPool;

    @Override
    public void run() {
        parse(message , channel);
    }
    public abstract void parse(Protoc.Message object , Channel channel);

    public void initData(Protoc.Message message, Channel channel, ThreadPool threadPool){
        this.message = message;
        this.channel = channel;
        this.threadPool = threadPool;
    }
}
