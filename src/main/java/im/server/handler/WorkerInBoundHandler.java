package im.server.handler;

import com.google.gson.Gson;

import im.core.container.Container;
import im.core.executor.ThreadPool;
import im.core.executor.define.Parse;
import im.protoc.protocolbuf.Protoc;
import im.utils.CommUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class WorkerInBoundHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LogManager.getLogger(WorkerInBoundHandler.class);

    private final ThreadPool threadPool;

    public WorkerInBoundHandler(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    //private static final Publisher publisher=Publisher.newInstance();
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(Container.getCount());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Protoc.Message) {
            Protoc.Message message = (Protoc.Message) msg;
            Container.pingPongRest(ctx.channel().id());
            threadPool.parseMessage(message, ctx.channel());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel read complete");
        ctx.flush();
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelWritabilityChanged");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error("something wrong {}", cause.getMessage());
        Container.logOut(ctx.channel().id());
        Container.removeChannel(ctx.channel());
        ctx.channel().close();
        //publisher.close();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().id() + " already offline");
        Container.logOut(ctx.channel().id());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Container.logOut(ctx.channel().id());
        super.channelInactive(ctx);
    }


    /**
     * heartbeat : when WRITE_IDLE is detect then send a PING message, while 3 times not receive PONG message then close this channel
     * if the client is not had a handshake so in the first WRITE_IDLE close it.
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        try {
            ByteBuf heartBeatBuf = Unpooled.directBuffer();
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent idle = (IdleStateEvent) evt;
                if (idle.state().equals(IdleState.WRITER_IDLE)) {
                    if (!Container.isLogin(ctx.channel().id())) {
                        logger.info("this client had not handshake , close it ");
                        ctx.channel().close();
                    } else {
                        String sendMsg = CommUtil.createHeartBeatMessage();
                        heartBeatBuf.writeBytes(sendMsg.getBytes());
                        Protoc.Message.Builder message_builder = Protoc.Message.newBuilder();
                        Protoc.Message.Head.Builder head_builder=Protoc.Message.Head.newBuilder();
                        head_builder.setType(Protoc.Message.type.PING);
                        head_builder.setStatus(Protoc.Message.status.REQ);
                        head_builder.setUid(UUID.randomUUID().toString());
                        message_builder.setHead(head_builder);
                        message_builder.setBody("");
                        Container.sendHeartBeat(message_builder.build(), ctx.channel().id());
                    }
                } else if (idle.state().equals(IdleState.READER_IDLE)) {
                    Container.pingPongCountAdd(ctx.channel().id());
                    if (Container.getPingPongCount(ctx.channel().id()) == 4) {
                        Container.logOut(ctx.channel().id());
                        ctx.channel().close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
