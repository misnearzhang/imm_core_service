package im.server;


import im.config.SystemConfig;
import im.server.handler.WorkOutBoundHandler;
import im.server.handler.WorkerInBoundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bootstramp 类 开启线程池 加载主服务
 *
 * @author Misnearzhang
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

    private final Logger logger = LogManager.getLogger(Server.class);

    private ApplicationContext springContext;

    public void bind(int port, final String delimit, final int idleRead, final int idleWrite) throws Exception {
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup slaver = new NioEventLoopGroup(4);
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(master, slaver);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ByteBuf delimiter = Unpooled.copiedBuffer(delimit.getBytes());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new LineBasedFrameDecoder(1024 * 5));
                    ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(
                            idleRead, idleWrite, 0));
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
            logger.info(port);
            logger.info("server has startup successful!");
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("server has stop !");
        } finally {
            master.shutdownGracefully();
            slaver.shutdownGracefully();
        }
    }

    public void init(){
        SpringInit();
    }
    public void startup(){
    }

    private void SpringInit() {
        //初始化spring
        springContext=new ClassPathXmlApplicationContext("applicationContext.xml");
    }


    public static void main(String[] args) {
        try {
            Server server=new Server();
            System.out.println("test");
            server.init();
            System.out.println("test1");
            server.bind(3000,"\r\n",205,200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
