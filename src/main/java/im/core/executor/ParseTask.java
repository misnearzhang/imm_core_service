package im.core.executor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import im.config.SystemConfig;
import im.core.container.Container;
import im.core.exception.NotOnlineException;
import im.core.executor.define.AbstractParse;
import im.protoc.*;
import im.protoc.db.OfflineMessage;
import im.protoc.protocolbuf.Protoc;
import im.support.mq.SendMessage;
import im.utils.CommUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 解析报文 并做处理  用户消息则将消息处理了转发给网络发送线程 系统消息则根据消息类型处理
 * Created by zhanglong on 17-2-25.
 */

public class ParseTask extends AbstractParse {
    @Autowired
    SystemConfig systemConfig;
    @Autowired
    SendMessage sender;
    private final Logger logger = LogManager.getLogger(ParseTask.class);
    private Gson gson = new Gson();
    private ThreadPool threadPool;

    boolean checkHandShake(String account, String password) {

        return true;
    }

    @Override
    public void parse(Protoc.Message message , Channel channel) {
        try {
            Protoc.Head head = message.getHead();
            Protoc.type type = head.getType();
            switch (type) {
                case USER:
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
                    if (toChannelId == null) {
                        Container.send(CommUtil.createResponse(MessageEnum.status.OFFLINE.getCode(), head.getUid()), fromChannelId);
                        logger.info("this account is offline and we will cache this message utils it online");
                        sender.send2db(new OfflineMessage().
                                setMessageContent(userMessage.getContent()).
                                setMessageFrom(userMessage.getFrom()).
                                setMessageTo(userMessage.getTo()).setMessageStatus("100").
                                setAddtime(new Date()).
                                setUpdatetime(new Date())
                        );
                        //缓存消息
                        //send2mq
                    } else {
                        Container.send(CommUtil.createResponse(MessageEnum.status.OK.getCode(), head.getUid()), fromChannelId);
                        threadPool.sendMessageNow(new SendTask(gson.toJson(message.getBody()), ThreadPool.RetransCount.FISRT, toChannelId, head.getUid()), head.getUid());
                    }
                    break;
                case SYSTEM:
                    //校验
                    //第一类 登录消息  拿到用户的登录信息 然后通过mq与webService通信 这边的公钥对吧webService的秘钥 判断登录情况
                    //第二类消息 登出消息 用户发送登出请求 服务器登出 注销用户链接
                    //。。。。
                    //系统消息 做出相应处理，比如说用户跳出
                    break;
                case HANDSHAKE:
                    logger.info("HANDSHAKE start");
                    String body = message.getBody();
                    logger.info("read the HANDSHAKE message:" + body);
                    HandShakeMessage handshakeMessage = gson.fromJson(body, HandShakeMessage.class);
                    //校验握手信息
                    if (!checkHandShake(handshakeMessage.getAccount(), handshakeMessage.getPassword())) {
                        //响应握手失败
                        logger.info("handshake Fail!!");
                        Container.send(CommUtil.createHandShakeResponse(MessageEnum.status.HANDSHAKEFAIL.getCode(), head.getUid()), channel.id());
                    } else {
                        //此处判断该用户是否已经在线 如已经在线 则发送下线通知 并更新Channel容器
                        String account = handshakeMessage.getAccount();
                        if (Container.isLogin(account)) {
                            //用户已经在线 向该用户发送下线通知
                            logger.info("this account already online , going well offline it");
                            ChannelId channelId = Container.getChannelId(account);
                            Channel channel1 = Container.getChannel(channelId);
                            Container.send(CommUtil.createPush(MessageEnum.status.OTHERLOGIN.getCode()), channelId);//发送下线通知
                            if(channel1!=null){
                                Container.removeChannel(channel);
                            }
                        }
                        logger.info("handshake Success!!");
                        Container.addChannel(channel);
                        Container.addOrReplace(handshakeMessage.getAccount(), channel.id());
                        Container.send(CommUtil.createHandShakeResponse(MessageEnum.status.OK.getCode(), head.getUid()), channel.id());
                    }
                    break;
                case RESPONSE:
                    //收到响应  判断响应类型  消息响应和心跳响应
                    //retransConcurrentHashMap.remove(header.getUid());
                    logger.info("receive response , remove retrans task");
                    threadPool.removeFurure(head.getUid());
                    break;
                case PONG:
                    //心跳响应  不做任何事
                    //Container.pingPongRest(ctx.channel().id());
                    break;
                default:
                    //不支持该协议
            }
        } catch (NullPointerException nullpoint) {
            logger.error("null message");
            //Container.send(CommUtil.createResponse(MessageEnum.status.ERROR.getCode(),null), channel.id());//空消息
            nullpoint.printStackTrace();
        } catch (NotOnlineException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
