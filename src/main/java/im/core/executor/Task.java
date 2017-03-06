package im.core.executor;

import im.protoc.Message;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhanglong on 17-2-25.
 */
public class Task implements Runnable{
    private Message message;

    public Task(Message message) {
        this.message = message;
    }

    public void run() {
        //TODO  消息处理
        WorkThread.retransExecutor.schedule(new RetransTask("first retrans", WorkThread.RetransCount.FISRT),1, TimeUnit.SECONDS);
    }
}
