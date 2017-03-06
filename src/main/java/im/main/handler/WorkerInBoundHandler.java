package im.main.handler;

import com.google.gson.Gson;

import im.core.container.Container;
import im.core.executor.Task;
import im.core.executor.WorkThread;
import im.protoc.Message;
import im.utils.CommUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class WorkerInBoundHandler extends ChannelInboundHandlerAdapter{
	
	Gson gson=new Gson();
	ByteBuf buf=Unpooled.copiedBuffer(("").getBytes());
	int i=0;
	private ByteBuf nativeByteBuf = Unpooled.copiedBuffer("".getBytes());
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Container.addChannel(ctx.channel());
		i=Container.getCount();
		Container.addOrReplace("zhanglong"+i, ctx.channel().id());
		buf.clear();
		buf=Unpooled.copiedBuffer("wellcome".getBytes());
		Container.send(buf,ctx.channel().id());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String message=msg.toString();
		System.out.println("ctx = [" + ctx + "], msg = [" + msg + "]");
		Message request=null;
		if(message!=null){
			request = CommUtil.varify(message);
			buf.clear();
			buf=Unpooled.copiedBuffer((gson.toJson("hello")+"\r\n").getBytes());
			if (request != null) {
				// 消息有效 放入消息队列并发送响应给用户
				WorkThread.executor.execute(new Task(request));
			} else {
				// 消息无效 快速响应

			}
			Container.send(buf,ctx.channel().id());
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("channelReadComplete");
		ctx.flush();
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("channelWritabilityChanged");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(cause.getMessage());
		System.out.println("发生异常  :"+ctx.hashCode());
		ctx.channel().close();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ctx.hashCode()+"已经下线下线");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ctx.hashCode()+"正在下线...");
		super.channelInactive(ctx);
	}


	/**
	 * 心跳设计   Read 120   Write 125  首先发生读心跳事件
	 * @param ctx
	 * @param evt
	 * @throws Exception
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		try {
			if (evt instanceof IdleStateEvent) {
				IdleStateEvent idle = (IdleStateEvent) evt;
				System.out.println(idle.state());
				System.out.println(Container.getPingPongCount(ctx.channel().id()));
				if (idle.state().equals(IdleState.WRITER_IDLE)) {
					//读超时 说明客户端没有活动  那么发送一个心跳
					nativeByteBuf.clear();
					nativeByteBuf=Unpooled.copiedBuffer("Hearbeat   please ignore".getBytes());
					Container.send(nativeByteBuf, ctx.channel().id());
				} else if (idle.state().equals(IdleState.READER_IDLE)) {
					if (Container.getPingPongCount(ctx.channel().id()) == 2) {
						//超时过多  不可用
						Container.logOut(ctx.channel().id());
						ctx.channel().close();
					} else {
						//写超时 说明心跳发送出去5秒没有收到响应 失败计数器加1
						Container.pingPongCountAdd(ctx.channel().id());
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
