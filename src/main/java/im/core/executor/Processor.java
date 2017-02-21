package im.core.executor;


import im.core.container.Container;
import im.core.queue.MQueue;
import im.db.SpringContext;
import im.db.service.IRequestService;
import im.protoc.Request;
import im.utils.CommUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
/**
 * 消息转发处理器
 * @author Misnearzhang
 *
 */
public class Processor implements Runnable {
	private IRequestService requestService;
	
	public Processor() {
		requestService = (IRequestService) SpringContext.ac.getBean("requestService");
	}

	public void run() {
		ByteBuf buf = null;
		while (true) {
			Request req = MQueue.get();
			if (req != null) {
				String to=req.getTo();
				String from = req.getFrom();
				String content = req.getContent();
				ChannelId toChannelId= Container.getChannelId(to);
				ChannelId fromChannelId=Container.getChannelId(from);
				if(toChannelId!=null){
					buf=Unpooled.copiedBuffer(("来自"+from+":"+content+"\r\n").getBytes());
					Container.send(buf, toChannelId);
				}else{
					/*
					 * 好友不在线 消息存入数据库
					 */
					buf=Unpooled.copiedBuffer(("you friends "+ to +"has already offline , but we can keep this message next time"+"\r\n").getBytes());
					Container.send(buf, fromChannelId);
					requestService.addRequest(CommUtil.Protoc2POJO(req));//存储数据库
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
