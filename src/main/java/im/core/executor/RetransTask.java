package im.core.executor;

import im.config.SystemConfig;
import im.core.container.Container;
import im.protoc.Message;

import java.util.concurrent.TimeUnit;

/**
 * Created by Misnearzhang on 2017/3/6.
 */
public class RetransTask implements Runnable{
    private Message message;
    private ThreadPool.RetransCount count;

    public RetransTask(Message Message, ThreadPool.RetransCount count) {
        this.message = Message;
        this.count = count;
    }

    public void run() {
        //Container.send();  send message
        switch (count){
            case FISRT:
                ThreadPool.retransExecutor.schedule(new RetransTask(this.message, ThreadPool.RetransCount.SECOND), SystemConfig.threadRetransSecondTime, TimeUnit.SECONDS);
                break;
            case SECOND:
                ThreadPool.retransExecutor.schedule(new RetransTask(this.message, ThreadPool.RetransCount.THIRD),SystemConfig.threadRetransThirdTime, TimeUnit.SECONDS);
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
