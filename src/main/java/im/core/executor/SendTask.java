package im.core.executor;

import im.config.SystemConfig;
import im.protoc.Message;
import io.netty.channel.ChannelId;

import java.util.concurrent.TimeUnit;

/**
 * Created by Misnearzhang on 2017/3/6.
 */
public class SendTask implements Runnable{
    private ChannelId toChannelId;
    private String uid;
    private Message message;
    private ThreadPool.RetransCount count;

    public SendTask(Message Message, ThreadPool.RetransCount count,ChannelId channelId,String uid) {
        this.message = Message;
        this.count = count;
        this.toChannelId=channelId;
        this.uid=uid;
    }

    public void run() {
        //Container.send();  send message
        switch (count){
            case FISRT:
                this.count=ThreadPool.RetransCount.SECOND;
                ThreadPool.sendMessage(this,uid);
                break;
            case SECOND:
                this.count=ThreadPool.RetransCount.THIRD;
                ThreadPool.sendMessage(this,uid);
                break;
            case THIRD:
                //发送到数据库保存  作为离线消息 同时关闭该channel
                //Publisher publisher= (Publisher) SpringBeanUtil.getBean("publisher");
                try {
                    //publisher.sendMessage("hello");
                } finally {

                }
                break;
            default:
        }
    }
}
