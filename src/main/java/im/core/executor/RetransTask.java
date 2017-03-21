package im.core.executor;

import im.config.SystemConfig;

import java.util.concurrent.TimeUnit;

/**
 * Created by Misnearzhang on 2017/3/6.
 */
public class RetransTask implements Runnable{
    private String string;
    private ThreadPool.RetransCount count;

    public RetransTask(String string, ThreadPool.RetransCount count) {
        this.string = string;
        this.count = count;
    }

    public void run() {
        switch (count){
            case FISRT:
                ThreadPool.retransExecutor.schedule(new RetransTask("second retrans", ThreadPool.RetransCount.SECOND), SystemConfig.threadRetransSecondTime, TimeUnit.SECONDS);
                break;
            case SECOND:
                ThreadPool.retransExecutor.schedule(new RetransTask("Third retrans", ThreadPool.RetransCount.THIRD),SystemConfig.threadRetransThirdTime, TimeUnit.SECONDS);
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
