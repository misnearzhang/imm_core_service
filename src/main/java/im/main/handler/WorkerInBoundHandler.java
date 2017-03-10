package im.main.handler;

import com.google.gson.Gson;

import im.core.container.Container;
import im.core.executor.Task;
import im.core.executor.EnumType;
import im.protoc.Message;
import im.utils.CommUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class WorkerInBoundHandler extends ChannelInboundHandlerAdapter{
	private final Logger logger = LogManager.getLogger( WorkerInBoundHandler.class );

	Gson gson=new Gson();
	ByteBuf buf=Unpooled.copiedBuffer(("").getBytes());
	AtomicInteger i=new AtomicInteger(0);
	private ByteBuf nativeByteBuf = Unpooled.copiedBuffer("".getBytes());
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Container.addChannel(ctx.channel());
		i.addAndGet(1);
		Container.addOrReplace("zhanglong"+i, ctx.channel().id());
		buf.clear();
		buf=Unpooled.copiedBuffer("wellcome".getBytes());
		Container.send(null,buf,ctx.channel().id());
		logger.info(Container.getCount());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String message=msg.toString();
		logger.info(message);
		Message request=null;
		try {
			if (message != null) {
				//request = CommUtil.varify(message);
				buf.clear();
				buf = Unpooled.copiedBuffer((gson.toJson("hello every one") + "\r\n").getBytes());
				if (request != null) {
					// 消息有效 放入消息队列并发送响应给用户
					EnumType.executor.execute(new Task(request));
				} else {
					// 消息无效 快速响应

				}
				Container.send(null, buf, ctx.channel().id());
			}
		}catch (Exception e){
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
		ctx.channel().close();
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
			if (evt instanceof IdleStateEvent) {
				IdleStateEvent idle = (IdleStateEvent) evt;
				if (idle.state().equals(IdleState.WRITER_IDLE)) {
					//读超时 说明客户端没有活动  那么发送一个心跳
					logger.info("write trigger ,send a request heartbeat");
					nativeByteBuf.clear();
					nativeByteBuf=Unpooled.copiedBuffer("Hearbeat   please ignore".getBytes());
					Container.send(null,nativeByteBuf, ctx.channel().id());
				} else if (idle.state().equals(IdleState.READER_IDLE)) {
					Container.pingPongCountAdd(ctx.channel().id());
					if (Container.getPingPongCount(ctx.channel().id()) == 3) {
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
