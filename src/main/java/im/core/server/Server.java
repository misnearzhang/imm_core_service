package im.core.server;


import im.process.ThreadPool;
import im.protoc.protocolbuf.Protoc;
import im.core.server.handler.WorkOutBoundHandler;
import im.core.server.handler.WorkerInBoundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private ThreadPool threadPool;
    public void setThreadPool(ThreadPool threadPool){
        this.threadPool = threadPool;
    }


    public void bind(int port, final int idleRead, final int idleWrite) throws Exception {
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
                    ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(
                            idleRead, idleWrite, 0));
/*                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                    ch.pipeline().addLast(new LineBasedFrameDecoder(1024 * 5));*/
                    // 设置protobuf编码器
                    ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
                    // 设置带长度解码器
                    ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(
                            Protoc.Message.getDefaultInstance()));
                    ch.pipeline().addLast(new WorkOutBoundHandler());
                    ch.pipeline().addLast(new WorkerInBoundHandler(threadPool));
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
}
