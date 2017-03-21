package im.core.executor;

import java.util.concurrent.*;

/**
 * 线程池 发送线程池  重传线程池
 * @author Misnearzhang
 *
 */
public class ThreadPool {

	public enum RetransCount{
		FISRT,SECOND,THIRD
	}
	private static BlockingQueue<Runnable> queue=new ArrayBlockingQueue<Runnable>(1000000);
	/**
	 * 核心线程池，corePoolSize maximumPoolSize keepAliveTime TimeUnit queue
	 */
	public static ThreadPoolExecutor executor=new ThreadPoolExecutor(20,50,10, TimeUnit.SECONDS,queue);

	/**
	 * 重传定时线程池
	 */
	public static ScheduledThreadPoolExecutor retransExecutor=new ScheduledThreadPoolExecutor(5);

}