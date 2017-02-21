package im.core.executor;

import im.core.queue.MQueue;
import im.protoc.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程模型2
 * @author Misnearzhang
 *
 */
public class WorkThread implements Runnable{

	private ExecutorService executor=Executors.newCachedThreadPool();
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			Request req = MQueue.get();
			executor.execute(new Processor2(req));
		}
	}

}
