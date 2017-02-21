package im.main;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import im.core.executor.WorkThread;
import im.main.handler.WorkOutBoundHandler;
import im.main.handler.WorkerInBoundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
/**
 * bootstramp 类 读取spring配置文件  开启线程池 加载主服务 
 * @author Misnearzhang
 *
 */
/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         		佛祖保佑       永无BUG
         			O0o li1
*/
public class Server {
	private static final byte[] SNMP_HOST_ADDR = {(byte)192, (byte)168, (byte)1, (byte)147};
	//private static final byte[] SNMP_HOST_ADDR = {(byte)104, (byte)251, (byte)225, (byte)127};
	private final static int SNMP_TRAP_PORT = 3000;
	public void bind(int port) throws Exception {
		final SocketAddress socket =
	            new InetSocketAddress(InetAddress.getByAddress(SNMP_HOST_ADDR), SNMP_TRAP_PORT);
		EventLoopGroup master = new NioEventLoopGroup(1);
		EventLoopGroup slaver = new NioEventLoopGroup(4);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(master, slaver);
			b.channel(NioServerSocketChannel.class);
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
					ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new LineBasedFrameDecoder(1024*5));
//					ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(10*1024*1024, 0, 2));
//					ch.pipeline().addLast(new StringDecoder());
//					ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(
//							180, 0, 0));
					ch.pipeline().addLast(new WorkOutBoundHandler());
					ch.pipeline().addLast(new WorkerInBoundHandler());
//					ByteBuf delimiter=Unpooled.copiedBuffer("".getBytes());
					/*// ch.pipeline().addLast(new ValidateUser());
					ChannelPipeline pipeline = ch.pipeline();
					// 设置带长度编码器
					pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
					// 设置protobuf编码器
					pipeline.addLast("protobufEncoder", new ProtobufEncoder());
					// 设置带长度解码器
					pipeline.addLast("frameDecoder",
							new LengthFieldBasedFrameDecoder(10485076, 0, 4, 0, 4));
					pipeline.addLast("idleStateHandler", new IdleStateHandler(
							180, 0, 0));
					// 设置protobufDecoder 解码器
					pipeline.addLast("protobufDecoder", new ProtobufDecoder(
							NettyProbuf.Netty.getDefaultInstance()));*/
					//pipeline.addLast(new WorkerInBoundHandler());
				}
			});
			ChannelFuture f = b.bind(3000).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			master.shutdownGracefully();
			slaver.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		try {
			//ExecutorService executor=Executors.newCachedThreadPool();
			//
			/*for(int i=0;i<20;i++){
				executor.execute(new Processor());//业务处理线程池
			}*/
			new Server().bind(3000);
			new Thread(new WorkThread()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
