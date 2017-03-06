package im.core.executor;

import im.support.mq.Publisher;
import im.utils.SpringBeanUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Misnearzhang on 2017/3/6.
 */
public class RetransTask implements Runnable{
    private String string;
    private WorkThread.RetransCount count;

    public RetransTask(String string, WorkThread.RetransCount count) {
        this.string = string;
        this.count = count;
    }

    public void run() {
        System.out.println(string);
        switch (count){
            case FISRT:
                WorkThread.retransExecutor.schedule(new RetransTask("second retrans", WorkThread.RetransCount.SECOND),2, TimeUnit.SECONDS);
                break;
            case SECOND:
                WorkThread.retransExecutor.schedule(new RetransTask("Third retrans", WorkThread.RetransCount.THIRD),4, TimeUnit.SECONDS);
                break;
            case THIRD:
                //发送到数据库保存  作为离线消息
                Publisher publisher= (Publisher) SpringBeanUtil.getBean("publisher");
                try {
                    publisher.sendMessage("hello");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }
}
