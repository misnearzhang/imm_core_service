package im.process;

import im.core.container.Container;
import im.core.exception.NotOnlineException;
import im.core.server.ThreadPool;
import im.protoc.protocolbuf.Protoc;
import io.netty.channel.ChannelId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Misnearzhang on 2017/3/6.
 */
public class SendTask implements Runnable {
    private final Logger logger = LogManager.getLogger(SendTask.class);
    private ChannelId toChannelId;
    private String uid;
    private Protoc.Message message;
    private ThreadPool threadPool;
    private ThreadPool.RetransCount count;

    public SendTask(ThreadPool threadPool, Protoc.Message Message, ThreadPool.RetransCount count, ChannelId channelId, String uid) {
        this.threadPool = threadPool;
        this.message = Message;
        this.count = count;
        this.toChannelId = channelId;
        this.uid = uid;
    }

    public void run() {
        logger.info(message + "----" + count.toString());
        try {
            Container.send(message, toChannelId);  //send message
        } catch (NotOnlineException e) {
            e.printStackTrace();
            return;
        }
        switch (count) {
            case FISRT:
                logger.info("first retrans");
                this.count = ThreadPool.RetransCount.SECOND;
                threadPool.sendMessage(this, uid);
                break;
            case SECOND:
                logger.info("second retrans");
                this.count = ThreadPool.RetransCount.THIRD;
                threadPool.sendMessage(this, uid);
                break;
            case THIRD:
                logger.info("give up,save in db");
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
