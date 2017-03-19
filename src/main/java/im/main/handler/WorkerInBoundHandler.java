package im.main.handler;

import com.google.gson.Gson;

import im.core.container.Container;
import im.core.executor.Task;
import im.core.executor.EnumType;
import im.protoc.Message;
import im.protoc.UserMessage;
import im.protoc.pojo.OfflineMessage;
import im.protoc.pojo.RequestPOJO;
import im.support.mq.Publisher;
import im.utils.CommUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerInBoundHandler extends ChannelInboundHandlerAdapter{
	private final Logger logger = LogManager.getLogger( WorkerInBoundHandler.class );

	private final Gson gson=new Gson();
	private ByteBuf buf=Unpooled.directBuffer();
	private static final Publisher publisher=Publisher.newInstance();
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Container.addChannel(ctx.channel());
		Container.addOrReplace("zhanglong"+System.currentTimeMillis(), ctx.channel().id());
		buf.clear();
		buf=Unpooled.copiedBuffer("wellcome\r\n".getBytes());
		Container.send(null,buf,ctx.channel().id());
		logger.info(Container.getCount());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String message=msg.toString();
		logger.info("心跳返回：:"+(String)msg);
		Message request=null;
		try {
			OfflineMessage message1=new OfflineMessage();
			message1.setMessageFrom(12);
			message1.setMessageTo(34);
			message1.setMessageStatus("1");
			message1.setAddtime(new Date());
			message1.setUpdatetime(new Date());
			message1.setMessageContent("one two three four five six seven eight nine ten");

			publisher.send(gson.toJson(message1));
			/*if (message != null) {
				//request = CommUtil.varify(message);
				//buf = Unpooled.copiedBuffer((gson.toJson("hello every one") + "\r\n").getBytes());
				buf=Unpooled.directBuffer();
				byte[] sendMsg="hi 大家好".getBytes();
				buf.writeBytes(sendMsg);
				if (request != null) {
					// 消息有效 放入消息队列并发送响应给用户
					EnumType.executor.execute(new Task(request));
				} else {
					// 消息无效 快速响应

				}
				Container.send(null, buf, ctx.channel().id());
			}*/
		}catch (Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		logger.info("channel read complete");
		ctx.flush();
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		logger.info("channelWritabilityChanged");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		logger.error("something wrong "+cause.getMessage());
		Container.logOut(ctx.channel().id());
		ctx.channel().close();
		publisher.close();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		logger.info(ctx.channel().id()+" already offline");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}


	/**
	 * 心跳设计   Read 120   Write 125  首先发生写心跳事件  紧接着去读这个事件 的响应
	 * 超过5秒不通就叫做一次失败  失败3次断掉链接 等待客户端重连
	 * @param ctx
	 * @param evt
	 * @throws Exception
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		try {
			ByteBuf heartBeatBuf=Unpooled.directBuffer();
			if (evt instanceof IdleStateEvent) {
				IdleStateEvent idle = (IdleStateEvent) evt;
				if (idle.state().equals(IdleState.WRITER_IDLE)) {
					//读超时 说明客户端没有活动  那么发送一个心跳
					logger.info("write trigger ,send a request heartbeat");
					String sendMsg=CommUtil.createHeartBeatMessage();
					heartBeatBuf.writeBytes(sendMsg.getBytes());
					Container.sendHeartBeat(heartBeatBuf, ctx.channel().id());
				} else if (idle.state().equals(IdleState.READER_IDLE)) {
					logger.info("读超时---------》》");
					Container.pingPongCountAdd(ctx.channel().id());
					logger.info("heartbeat Count: "+Container.getPingPongCount(ctx.channel().id()) );
					if (Container.getPingPongCount(ctx.channel().id()) == 4) {
						//超时过多  不可用
						Container.logOut(ctx.channel().id());
						ctx.channel().close();
						logger.error("client no response 3 times.  So I have closed the channel and wait for reconnect");
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
