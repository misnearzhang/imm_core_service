package im.core.executor;

import com.google.gson.Gson;
import im.config.SystemConfig;
import im.core.container.Container;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.MessageEnum;
import im.protoc.UserMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelId;

import java.util.concurrent.TimeUnit;

import im.core.container.Container;

/**
 * 解析报文 并做处理  用户消息则将消息处理了转发给网络发送线程 系统消息则根据消息类型处理
 * Created by zhanglong on 17-2-25.
 */
public class ParseTask implements Runnable{
    private Gson gson =new Gson();
    private Message sendMessage;
    private String message;

    public ParseTask(String message) {
        this.message = message;
    }

    public void run() {
        //TODO  消息处理
        Header header= gson.fromJson(sendMessage.getHead(),Header.class);
        String type=header.getType();
        ByteBuf sendBuf= Unpooled.copiedBuffer("".getBytes());
        /*
        USER( "user", "用户消息" ),
        SYSTEM( "system", "系统消息" ),
        RESPONSE( "response", "响应消息" )
         */
        if("user".equals(type)){
            //解析出发送方
            String from;
            String to;
            String uid=header.getUid();
            UserMessage userMessage=gson.fromJson(sendMessage.getBody(),UserMessage.class);
            from=userMessage.getFrom();
            to=userMessage.getTo();
            ChannelId channelId=Container.getChannelId(to);
            userMessage.setFrom(to);
            userMessage.setTo(from);
            Header header1=new Header();
            header1.setStatus("");
            header1.setUid(header.getUid());
            header1.setType(MessageEnum.type.USER.getCode());
            sendMessage.setHead(gson.toJson(header1));
            sendMessage.setBody(gson.toJson(userMessage));
            ThreadPool.sendMessage(new SendTask(sendMessage, ThreadPool.RetransCount.FISRT,channelId,uid),uid);
        }else if("system".equals(type)){
            //系统消息 做出相应处理，比如说用户跳出

        }else if("response".equals(type)){
            //收到响应  判断响应类型  消息响应和心跳响应
            //retransConcurrentHashMap.remove(header.getUid());
            ThreadPool.removeFurure(header.getUid());
        }

    }
}
