package im.core.executor;

import im.support.mq.Publisher;
import im.utils.SpringBeanUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Misnearzhang on 2017/3/6.
 */
public class RetransTask implements Runnable{
    private String string;
    private EnumType.RetransCount count;

    public RetransTask(String string, EnumType.RetransCount count) {
        this.string = string;
        this.count = count;
    }

    public void run() {
        switch (count){
            case FISRT:
                EnumType.retransExecutor.schedule(new RetransTask("second retrans", EnumType.RetransCount.SECOND),2, TimeUnit.SECONDS);
                break;
            case SECOND:
                EnumType.retransExecutor.schedule(new RetransTask("Third retrans", EnumType.RetransCount.THIRD),4, TimeUnit.SECONDS);
                break;
            case THIRD:
                //发送到数据库保存  作为离线消息 同时关闭该channel
                Publisher publisher= (Publisher) SpringBeanUtil.getBean("publisher");
                try {
                    publisher.sendMessage("hello");
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                }
                break;
            default:
        }
    }
}
