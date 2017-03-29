package im.testclient;

import com.google.gson.Gson;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.MessageEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestTCPClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("from server:" + msg.toString());
		Gson gson = new Gson();
		Message message = gson.fromJson(msg.toString(), Message.class);
		Header header = message.getHead();
		if (MessageEnum.type.PING.getCode().equals(header.getType())) {
			Message response = new Message();
			Header header1 = new Header();
			header1.setUid(header.getUid());
			header1.setStatus("200");
			header1.setType(MessageEnum.type.PONG.getCode());
			message.setHead(header1);
			String send = gson.toJson(message);
			send += "\r\n";
			ByteBuf buf = Unpooled.copiedBuffer(send.getBytes());
			ctx.channel().writeAndFlush(buf);
		}else if(MessageEnum.type.USER.getCode().equals(header.getType())){
			Message response = new Message();
			Header header1 = new Header();
			header1.setUid(header.getUid());
			header1.setStatus("200");
			header1.setType(MessageEnum.type.RESPONSE.getCode());
			message.setHead(header1);
			String send = gson.toJson(message);
			send += "\r\n";
			ByteBuf buf = Unpooled.copiedBuffer(send.getBytes());
			ctx.channel().writeAndFlush(buf);
		}
		super.channelRead(ctx, msg);
	}

}
