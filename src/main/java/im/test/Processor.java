package im.test;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Processor implements Runnable {

	private BlockingQueue<String> queue;

	public Processor(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random r = new Random();
		boolean isRunning = true;
	/*	File file = new File("test.txt");
		FileOutputStream fos = null;
		BufferedOutputStream bos=null;
		PrintWriter writer=null;*/
		try {
			/*fos = new FileOutputStream(file);
			bos=new BufferedOutputStream(fos);
			writer=new PrintWriter(file);*/
			while (isRunning) {
				String data = queue.poll(2, TimeUnit.HOURS);
				if (null != data) {
					System.out.println("ThreadName:" + Thread.currentThread().getName());
					/*writer.println(Thread.currentThread().getName()+"  size:"+queue.size());
					*/
					Thread.sleep(100);
				} else {
					System.out.println("now data");
					isRunning = false;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			System.out.println("退出消费者线程！");
		}
	}

}
