package im.testclient;

import com.google.gson.Gson;
import im.protoc.protocolbuf.Protoc;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestTCPClient implements Runnable{
	private final Logger logger = LogManager.getLogger(TestTCPClient.class);
	public Channel channel;
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
							1000, 999, 0));
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(
							Protoc.Message.getDefaultInstance()));
					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
					ch.pipeline().addLast(new TestTCPHandler());
				}
			});

			ChannelFuture f=b.connect("127.0.0.1", 22222).sync();
			channel = f.channel();
			if(channel!=null&&channel.isActive()){
				logger.debug("connecting successful");
			}
			channel.writeAndFlush(SendHandshake());
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Gson gson = new Gson();
		final TestTCPClient client = new TestTCPClient();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100,500,1, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10000));
		executor.execute(client);
	}

	public static byte[] SendHandshake(){
		String hello = "0501helahekrhfaeohwehhasjdsjjshdh----------------------";
		/*Gson gson = new Gson();
		Protoc.Message.Builder Proto = Protoc.Message.newBuilder();
		Protoc.Head.Builder Head = Protoc.Head.newBuilder();
		Head.setType(Protoc.type.HANDSHAKE);
		Head.setStatus(Protoc.status.REQ);
		Head.setUid(UUID.randomUUID().toString());
		Head.setTime(System.currentTimeMillis());
		Proto.setHead(Head);
		HandShakeMessage handShakeMessage1 = new HandShakeMessage();
		handShakeMessage1.setAccount("1065302407");
		handShakeMessage1.setPassword("123456");
		Proto.setBody(gson.toJson(handShakeMessage1));
		return Proto.build();*/
		return hello.getBytes();
	}

}
