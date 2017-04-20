package im.core.executor;

import im.config.SystemConfig;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

/**
 * 线程池 发送线程池  重传线程池
 *
 * @author Misnearzhang
 */
public class ThreadPool {
    public enum RetransCount {
        FISRT, SECOND, THIRD
    }

    private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1000000);
    /**
     * parse线程池，corePoolSize maximumPoolSize keepAliveTime TimeUnit queue
     */
    private static ThreadPoolExecutor parser = new ThreadPoolExecutor(10, 20, 5, TimeUnit.SECONDS, queue);

    /**
     * 发送线程池
     */
    private static ScheduledThreadPoolExecutor retransExecutor = new ScheduledThreadPoolExecutor(5);
    private static ConcurrentHashMap<String, ScheduledFuture> futures = new ConcurrentHashMap<String, ScheduledFuture>(100);

    static {
        retransExecutor.setRemoveOnCancelPolicy(true);
    }

    public static void parseMessage(String message, Channel channel) {
        parser.execute(new ParseTask(message, channel));
    }


    /**
     * 立即发送消息
     *
     * @param task
     * @param uid
     */
    public static void sendMessageNow(SendTask task, String uid) {
        ScheduledFuture future = retransExecutor.schedule(task, 0, TimeUnit.SECONDS);
        futures.put(uid, future);
    }

    /**
     * 重发消息
     *
     * @param task
     * @param uid
     */
    public static void sendMessage(SendTask task, String uid) {
        ScheduledFuture future = retransExecutor.schedule(task, 3, TimeUnit.SECONDS);
        futures.put(uid, future);

    }

    public static boolean removeFurure(String uid) {
        ScheduledFuture future = futures.get(uid);
        future.cancel(false);
        retransExecutor.purge();
        return true;
    }
}
