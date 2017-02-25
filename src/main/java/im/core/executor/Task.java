package im.core.executor;

import im.protoc.Request;

/**
 * Created by zhanglong on 17-2-25.
 */
public class Task implements Runnable{
    private Request request;

    public Task(Request request) {
        this.request = request;
    }

    public void run() {
        //TODO  消息处理
    }
}
