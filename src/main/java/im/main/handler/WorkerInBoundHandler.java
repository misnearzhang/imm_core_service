package im.main.handler;

import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;

import im.core.container.Container;
import im.core.queue.MQueue;
import im.protoc.Request;
import im.protoc.Response;
import im.utils.CommUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class WorkerInBoundHandler extends ChannelInboundHandlerAdapter{
	
	Gson gson=new Gson();
	ByteBuf buf=Unpooled.copiedBuffer(("").getBytes());;
	int i=0;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Container.addChannel(ctx.channel());
		i=Container.getCount();
		Container.addOrReplace("zhanglong"+i, ctx.channel().id());
		buf.clear();
		buf=Unpooled.copiedBuffer(("wellcome  zhanglong"+i+"\r\n").getBytes());
		ctx.writeAndFlush(buf);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String message=msg.toString();
		Request request=null;
		Response response=new Response();
		if(message!=null){
			request = CommUtil.varify(message);
			if (request != null) {
				// 消息有效 放入消息队列并发送响应给用户
				MQueue.put(request);
				response.setUid(request.getUid());
				response.setDes("OK");
				response.setStatus(200);
				response.setTimestamp(new Date().getTime());
				response.setUid(UUID.randomUUID().toString());
			} else {
				// 消息无效 只响应用户
				response.setUid(null);
				response.setDes("解码异常!");
				response.setStatus(500);
				response.setTimestamp(new Date().getTime());
				response.setUid(UUID.randomUUID().toString());
			}
			buf.clear();
			buf=Unpooled.copiedBuffer((gson.toJson(response)).getBytes());
			ctx.writeAndFlush(buf);
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
	
}
