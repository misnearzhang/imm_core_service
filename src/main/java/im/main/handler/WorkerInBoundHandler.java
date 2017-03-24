package im.main.handler;

import com.google.gson.Gson;

import im.core.container.Container;
import im.core.executor.ParseTask;
import im.core.executor.ThreadPool;
import im.protoc.Message;
import im.protoc.pojo.OfflineMessage;
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

public class WorkerInBoundHandler extends ChannelInboundHandlerAdapter{
	private final Logger logger = LogManager.getLogger( WorkerInBoundHandler.class );

	private final Gson gson=new Gson();
	private static final Publisher publisher=Publisher.newInstance();
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String userId="zhanglong"+System.currentTimeMillis();
		Container.addChannel(ctx.channel());
		Container.addOrReplace(userId, ctx.channel().id());
		Container.send("wellcome:"+userId+"\r\n",ctx.channel().id());
		logger.info(Container.getCount());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String message=msg.toString();
		ThreadPool.parseMessage(message);
		logger.info("收到消息：:"+(String)msg);
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
		Container.logOut(ctx.channel().id());
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Container.logOut(ctx.channel().id());
		super.channelInactive(ctx);
	}


	/**
	 * 心跳设计   Read 122   Write 120  首先发生写超时  接着像客户端发送心跳 等待心跳响应
	 * 超过2秒无响应失败计数器++ 失败3次断掉链接 等待客户端重连
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
