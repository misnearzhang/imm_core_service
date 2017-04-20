package im.core.executor;

import im.core.executor.define.AbstractParse;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 线程池 发送线程池  重传线程池
 *
 * @author Misnearzhang
 */
public class ThreadPool {

    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer keepAliveTime;
    private Integer blockingQueueSize;


    public ThreadPool(Integer corePoolSize, Integer maxPoolSize, Integer keepAliveTime, Integer blockingQueueSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.blockingQueueSize = blockingQueueSize;
    }

    public void init() {
        queue = new ArrayBlockingQueue<Runnable>(blockingQueueSize);
        businessExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
        ioExecutor= new ScheduledThreadPoolExecutor(5);
        ioExecutor.setRemoveOnCancelPolicy(true);
        futures = new ConcurrentHashMap<String, ScheduledFuture>(100);
    }

    public enum RetransCount {
        FISRT, SECOND, THIRD
    }

    private AbstractParse parse;

    private BlockingQueue<Runnable> queue;
    /**
     * parse线程池，corePoolSize maximumPoolSize keepAliveTime TimeUnit queue
     */
    private ThreadPoolExecutor businessExecutor;

    /**
     * 发送线程池
     */
    private ScheduledThreadPoolExecutor ioExecutor;
    private ConcurrentHashMap<String, ScheduledFuture> futures;

    /**
     * 业务处理类的全限定名
     *
     * @param name
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void reflectParse(String name) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(name);
        parse = (AbstractParse) clazz.newInstance();
    }

    public void parseMessage(String message, Channel channel) {
        parse.initData(message, channel);
        businessExecutor.execute(parse);
    }


    /**
     * 立即发送消息
     *
     * @param task
     * @param uid
     */
    public void sendMessageNow(SendTask task, String uid) {
        ScheduledFuture future = ioExecutor.schedule(task, 0, TimeUnit.SECONDS);
        futures.put(uid, future);
    }

    /**
     * 重发消息
     *
     * @param task
     * @param uid
     */
    public void sendMessage(SendTask task, String uid) {
        ScheduledFuture future = ioExecutor.schedule(task, 3, TimeUnit.SECONDS);
        futures.put(uid, future);

    }

    public boolean removeFurure(String uid) {
        ScheduledFuture future = futures.get(uid);
        future.cancel(false);
        ioExecutor.purge();
        return true;
    }
}
