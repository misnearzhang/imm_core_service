package im.core.executor;

import com.google.gson.Gson;
import im.core.container.Container;
import im.protoc.*;
import im.utils.CommUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 解析报文 并做处理  用户消息则将消息处理了转发给网络发送线程 系统消息则根据消息类型处理
 * Created by zhanglong on 17-2-25.
 */
public class ParseTask implements Runnable{
    private final Logger logger = LogManager.getLogger( ParseTask.class );
    private Gson gson =new Gson();
    private Message sendMessage;
    private String message;
    private Channel channel;
    public ParseTask(String message,Channel channel) {
        this.message = message;
        this.channel=channel;
    }

    public void run() {
        try {
            logger.info(message);
            sendMessage = gson.fromJson(message, Message.class);
            Header header = gson.fromJson(sendMessage.getHead(), Header.class);
            String uid = header.getUid();
            String type = header.getType();
            if ("user".equals(type)) {
                //解析出发送方
                String from;
                String to;
                UserMessage userMessage = gson.fromJson(sendMessage.getBody(), UserMessage.class);
                to = userMessage.getTo();
                from = userMessage.getFrom();
                ChannelId toChannelId = Container.getChannelId(to);
                ChannelId fromChannelId = Container.getChannelId(from);
                logger.info("to:" + to);
                logger.info("from:" + from);
                Container.send(CommUtil.createResponse(uid), fromChannelId);
                ThreadPool.sendMessageNow(new SendTask(message, ThreadPool.RetransCount.FISRT, toChannelId, uid), uid);
            } else if ("system".equals(type)) {
                String body=sendMessage.getBody();
                SystemMessage systemMessage=gson.fromJson(body,SystemMessage.class);
                if("handshake".equals(systemMessage.getType())){
                    HandShakeMessage handshakeMessage=(HandShakeMessage)systemMessage.getData();
                    Container.send(CommUtil.createResponse(uid), channel.id());
                    Container.addChannel(channel);
                    Container.addOrReplace(handshakeMessage.getAccount(), channel.id());
                    Container.addChannel(channel);
                }
                //校验
                //第一类 登录消息  拿到用户的登录信息 然后通过mq与webService通信 这边的公钥对吧webService的秘钥 判断登录情况
                //第二类消息 登出消息 用户发送登出请求 服务器登出 注销用户链接
                //。。。。
                //系统消息 做出相应处理，比如说用户跳出
            } else if ("response".equals(type)) {
                //收到响应  判断响应类型  消息响应和心跳响应
                //retransConcurrentHashMap.remove(header.getUid());
                logger.info("收到响应了 删去重发");
                ThreadPool.removeFurure(header.getUid());
            }else if(MessageEnum.type.PONG.getCode().equals(type)){
                //心跳响应  不做任何事
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
