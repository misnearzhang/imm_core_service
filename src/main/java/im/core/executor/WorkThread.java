package im.core.executor;

import java.util.concurrent.*;

/**
 * 线程模型2
 * @author Misnearzhang
 *
 */
public class WorkThread {

	private static BlockingQueue<Runnable> queue=new ArrayBlockingQueue<Runnable>(1000000);
	public static ThreadPoolExecutor executor=new ThreadPoolExecutor(20,50,10, TimeUnit.SECONDS,queue);


}
