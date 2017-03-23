package im.testclient;

import com.google.gson.Gson;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.MessageEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestTCPClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("from server:"+msg.toString());
		Gson gson=new Gson();
		Message message=gson.fromJson(msg.toString(),Message.class);
		Header header=gson.fromJson(message.getHead(),Header.class);
		if(MessageEnum.type.HEARTBEAT.getCode().equals(header.getType())){
			Message response=new Message();
			Header header1=new Header();
			header1.setUid(header.getUid());
			header1.setStatus("200");
			header1.setType(MessageEnum.type.HEARTBEAT.getCode());
			message.setHead(gson.toJson(header1));
			String send=gson.toJson(message);
			send+="\r\n";
			ByteBuf buf= Unpooled.copiedBuffer(send.getBytes());
			ctx.channel().writeAndFlush(buf);
		}
	}

}
