package im.core.queue;

import im.protoc.Request;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 消息队列  
 * @author Misnearzhang
 *
 */
public class MQueue {
	 private static BlockingQueue<Request> msgQueue = new ArrayBlockingQueue<Request>(100000);
	 
	 
	 public static boolean put(Request request) {
		 try {
			msgQueue.put(request);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	 }
	 public static Request get(){
		 try {
			return msgQueue.poll(1,TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	 }
}
