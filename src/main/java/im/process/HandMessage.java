package im.process;

import com.google.gson.Gson;
import im.core.container.Container;
import im.core.define.ParseReal;
import im.core.exception.NotOnlineException;
import im.core.server.ThreadPool;
import im.protoc.HandShakeMessage;
import im.protoc.SystemMessage;
import im.protoc.UserMessage;
import im.protoc.protocolbuf.Protoc;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * Created by zhanglong on 2017/8/1.
 */
public class HandMessage extends ParseReal {
    private final Logger logger = LogManager.getLogger(HandMessage.class);
    private Gson gson = new Gson();
    boolean checkHandShake(String account, String password) {

        return true;
    }
    @Override
    public void fromUser(Protoc.Message message, Channel channel) {
        //解析出发送方
        String from;
        String to;
        UserMessage userMessage = gson.fromJson(message.getBody(), UserMessage.class);
        to = userMessage.getTo();
        from = userMessage.getFrom();
        ChannelId toChannelId = Container.getChannelId(to);
        ChannelId fromChannelId = Container.getChannelId(from);
        logger.info("to:" + to);
        logger.info("from:" + from);
        try{
            if (toChannelId == null) {
                //Container.send(CommUtil.createResponse(MessageEnum.status.OFFLINE.getCode(), head.getUid()), fromChannelId);
                logger.info("this account is offline and we will cache this message utils it online");
                        /*sender.send2db(new OfflineMessage().
                                setMessageContent(userMessage.getContent()).
                                setMessageFrom(userMessage.getFrom()).
                                setMessageTo(userMessage.getTo()).setMessageStatus("100").
                                setAddtime(new Date()).
                                setUpdatetime(new Date())
                        );*/
                Protoc.Message.Builder response = Protoc.Message.newBuilder();
                Protoc.Head.Builder head1= Protoc.Head.newBuilder();
                head1.setTime(System.currentTimeMillis());
                head1.setUid(message.getHead().getUid());
                head1.setStatus(Protoc.status.OFFLINE);
                head1.setType(Protoc.type.RESPONSE);
                response.setHead(head1);
                Container.send(response.build(),channel.id());//发送响应用户离线消息
                //缓存消息
                //send2mq
            } else {
                Protoc.Message.Builder response = Protoc.Message.newBuilder();
                Protoc.Head.Builder head1= Protoc.Head.newBuilder();
                head1.setTime(System.currentTimeMillis());
                head1.setUid(message.getHead().getUid());
                head1.setStatus(Protoc.status.OK);
                head1.setType(Protoc.type.RESPONSE);
                response.setHead(head1);
                Container.send(response.build(),channel.id());//发送响应消息
                threadPool.sendMessageNow(new SendTask(threadPool,message, ThreadPool.RetransCount.FISRT, toChannelId, message.getHead().getUid()), message.getHead().getUid());
            }
        }catch (NotOnlineException e){
            System.out.println();
        }

    }

    @Override
    public void fromSystem(Protoc.Message message, Channel channel) {
        //校验
        //第一类 登录消息  拿到用户的登录信息 然后通过mq与webService通信 这边的公钥对吧webService的秘钥 判断登录情况
        //第二类消息 登出消息 用户发送登出请求 服务器登出 注销用户链接
        //。。。。
        //系统消息 做出相应处理，比如说用户登出
    }

    @Override
    public void handShake(Protoc.Message message, Channel channel) {
        logger.info("HANDSHAKE start");
        Protoc.Message.Builder handshake_resp = Protoc.Message.newBuilder();
        Protoc.Head.Builder handshake_head = Protoc.Head.newBuilder();
        handshake_head.setType(Protoc.type.RESPONSE);
        handshake_head.setUid(message.getHead().getUid());
        handshake_head.setTime(System.currentTimeMillis());
        String body = message.getBody();
        logger.info("read the HANDSHAKE message:" + body);
        HandShakeMessage handshakeMessage = gson.fromJson(body, HandShakeMessage.class);
        try{
            //校验握手信息
            if (!checkHandShake(handshakeMessage.getAccount(), handshakeMessage.getPassword())) {
                //响应握手失败
                logger.info("handshake Fail!!");
                handshake_head.setStatus(Protoc.status.HANDSHAKEFAIL);
            } else {
                //此处判断该用户是否已经在线 如已经在线 则发送下线通知 并更新Channel容器
                String account = handshakeMessage.getAccount();
                if (Container.isLogin(account)) {
                    //用户已经在线 向该用户发送下线通知
                    logger.info("this account already online , going well offline it");
                    ChannelId channelId = Container.getChannelId(account);
                    Channel channel1 = Container.getChannel(channelId);
                    //发送通知
                    Protoc.Message.Builder systemMessage = Protoc.Message.newBuilder();
                    Protoc.Head.Builder h = Protoc.Head.newBuilder();
                    h.setStatus(Protoc.status.REQ);
                    h.setType(Protoc.type.SYSTEM);
                    h.setUid(UUID.randomUUID().toString());
                    h.setTime(System.currentTimeMillis());
                    systemMessage.setHead(h);
                    SystemMessage offline_push = new SystemMessage();
                    offline_push.setTo("LLA");
                    offline_push.setType("LOGGINOTHER");
                    offline_push.setDesc("在其他地方有登录 请修改密码");
                    systemMessage.setBody(gson.toJson(offline_push));
                    Container.send(systemMessage.build(), channelId);//发送下线通知
                    channel1.close();//直接关闭链路
                    Container.logOut(channelId);
                }
                logger.info("handshake Success!!");
                Container.addChannel(channel);
                Container.addOrReplace(handshakeMessage.getAccount(), channel.id());
                handshake_head.setStatus(Protoc.status.OK);//handshake successful
            }
            handshake_resp.setHead(handshake_head);
            Container.send(handshake_resp.build(),channel.id());
        }catch (NotOnlineException e){
            System.out.println();
        }

    }
}
