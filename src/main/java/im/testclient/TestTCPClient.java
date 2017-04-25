package im.testclient;

import com.google.gson.Gson;
import com.rabbitmq.client.impl.AMQImpl;
import im.protoc.HandShakeMessage;
import im.protoc.protocolbuf.Protoc;
import im.server.handler.WorkOutBoundHandler;
import im.server.handler.WorkerInBoundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.UUID;
import java.util.concurrent.*;

public class TestTCPClient implements Runnable{
	public Channel channel;
	CountDownLatch count =new CountDownLatch(1);
	@Override
	public void run(){
		EventLoopGroup group=new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(
							10, 5, 0));
					// 设置protobuf编码器
					ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
					// 设置带长度解码器
					ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(
							Protoc.Message.getDefaultInstance()));
					ch.pipeline().addLast(new TestTCPHandler());
				}
			});
			ChannelFuture f=b.connect("127.0.0.1", 3000).sync();
			channel = f.channel();
			channel.writeAndFlush(SendHandshake());
			//count.countDown();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		final TestTCPClient client = new TestTCPClient();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100,500,1, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10000));
		for(int i=0;i<10000;i++){
			Thread.sleep(200);
			executor.execute(client);
		}
		/*client.count.await();
		client.channel.writeAndFlush(SendHandshake());*/
	}

	public static Protoc.Message SendHandshake(){
		Gson gson = new Gson();
		Protoc.Message.Builder Proto = Protoc.Message.newBuilder();
		Protoc.Head.Builder Head = Protoc.Head.newBuilder();
		Head.setType(Protoc.type.HANDSHAKE);
		Head.setStatus(Protoc.status.REQ);
		Head.setUid(UUID.randomUUID().toString());
		Head.setTime(System.currentTimeMillis());
		Proto.setHead(Head);
		HandShakeMessage handShakeMessage1 = new HandShakeMessage();
		handShakeMessage1.setAccount(System.currentTimeMillis()+"");
		handShakeMessage1.setPassword("123456");
		Proto.setBody(gson.toJson(handShakeMessage1));
		return Proto.build();
	}

}
