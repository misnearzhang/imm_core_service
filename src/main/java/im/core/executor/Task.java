package im.core.executor;

import com.google.gson.Gson;
import im.core.container.Container;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.MessageEnum;
import im.protoc.UserMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelId;

import java.util.concurrent.TimeUnit;

import static im.core.container.Container.retransConcurrentHashMap;
import static im.core.container.Container.send;

/**
 * Created by zhanglong on 17-2-25.
 */
public class Task implements Runnable{
    private Gson gson =new Gson();
    private Message message;

    public Task(Message message) {
        this.message = message;
    }

    public void run() {
        //TODO  消息处理
        Header header= gson.fromJson(message.getHead(),Header.class);
        String type=header.getType();
        ByteBuf sendBuf= Unpooled.copiedBuffer("".getBytes());
        /*
        USER( "user", "用户消息" ),
        SYSTEM( "system", "系统消息" ),
        RESPONSE( "response", "响应消息" ),
        HEARTBEAT( "heartbeat", "心跳消息" );
         */
        if("user".equals(type)){
            //解析出发送方
            String from;
            String to;
            String uid;
            UserMessage userMessage=gson.fromJson(message.getBody(),UserMessage.class);
            from=userMessage.getFrom();
            to=userMessage.getTo();
            ChannelId channelId=Container.getChannelId(to);
            userMessage.setFrom(to);
            userMessage.setTo(from);
            Header header1=new Header();
            header1.setStatus("");
            header1.setUid(header.getUid());
            header1.setType(MessageEnum.type.USER.getCode());
            message.setHead(gson.toJson(header1));
            message.setBody(gson.toJson(userMessage));
            //send
            sendBuf.skipBytes(gson.toJson(message).getBytes().length);
            send(header.getUid(),sendBuf,Container.getChannelId(to));
            retransConcurrentHashMap.put("",sendBuf);
        }else if("response".equals(type)){
            //收到响应  将
            retransConcurrentHashMap.remove(header.getUid());

        }else if("heartbeat".equals(type)){
            //直接丢弃 完成通信就可以了
        }
        ThreadPool.retransExecutor.schedule(new RetransTask("first retrans", ThreadPool.RetransCount.FISRT),2, TimeUnit.SECONDS);
    }
}
