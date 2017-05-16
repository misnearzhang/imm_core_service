package im.process;

import im.core.define.AbstractParse;
import im.protoc.protocolbuf.Protoc;
import io.netty.channel.Channel;
import java.util.concurrent.*;

/**
 * 线程池 发送线程池  重传线程池
 *
 * @author Misnearzhang
 */
public class ThreadPool {

    private Integer corePoolSize;//核心线程池大小
    private Integer maxPoolSize;//最大线程池大小
    private Integer keepAliveTime;//时间
    private Integer blockingQueueSize;//阻塞队列长度


    public ThreadPool(Integer corePoolSize, Integer maxPoolSize, Integer keepAliveTime, Integer blockingQueueSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.blockingQueueSize = blockingQueueSize;
    }

    public void init() {
        queue = new ArrayBlockingQueue(blockingQueueSize);
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
     * 业务处理类的全限定名 类似  ： com.xxx.***.ClassName
     *
     * @param clazz
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void reflectParse(Class clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        parse = (AbstractParse) clazz.newInstance();
    }

    public void parseMessage(Protoc.Message message, Channel channel) {
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

    /**
     * 移除重发队列
     * @param uid
     * @return
     */
    public boolean removeFuture(String uid) {
        if(futures.containsKey(uid)){
            ScheduledFuture future = futures.get(uid);
            future.cancel(false);
            ioExecutor.purge();
            return true;
        }
        return true;
    }

    public void close(){
        this.ioExecutor.shutdown();
        this.businessExecutor.shutdown();
    }
}
