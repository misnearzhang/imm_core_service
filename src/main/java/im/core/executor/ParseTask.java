package im.core.executor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import im.core.container.Container;
import im.core.exception.NotOnlineException;
import im.core.exception.UnSupportMessageType;
import im.core.executor.define.AbstractParse;
import im.core.executor.define.Parse;
import im.protoc.*;
import im.utils.CommUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 解析报文 并做处理  用户消息则将消息处理了转发给网络发送线程 系统消息则根据消息类型处理
 * Created by zhanglong on 17-2-25.
 */
public class ParseTask extends AbstractParse {
    private final Logger logger = LogManager.getLogger(ParseTask.class);
    private Gson gson = new Gson();
    private Message sendMessage;

    public ParseTask(String message, Channel channel) {
        super(message,channel);
    }

    boolean checkHandShake(String account, String password) {

        return true;
    }

    @Override
    public void parse(Object message,Channel channel) {
        try {
            sendMessage = (Message) message;
            Header header = sendMessage.getHead();
            String uid = header.getUid();
            String type = header.getType();
            switch (type) {
                case MessageEnum.TYPE_USER:
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
                    if (toChannelId == null) {
                        Container.send(CommUtil.createResponse(MessageEnum.status.OFFLINE.getCode(), uid), fromChannelId);
                        logger.info("this account is offline and we will cache this message utils it online");
                        //缓存消息
                        //send2mq
                    } else {
                        Container.send(CommUtil.createResponse(MessageEnum.status.OK.getCode(), uid), fromChannelId);
                        ThreadPool.sendMessageNow(new SendTask(gson.toJson(message), ThreadPool.RetransCount.FISRT, toChannelId, uid), uid);
                    }
                    break;
                case MessageEnum.TYPE_SYSTEM:
                    //校验
                    //第一类 登录消息  拿到用户的登录信息 然后通过mq与webService通信 这边的公钥对吧webService的秘钥 判断登录情况
                    //第二类消息 登出消息 用户发送登出请求 服务器登出 注销用户链接
                    //。。。。
                    //系统消息 做出相应处理，比如说用户跳出
                    break;
                case MessageEnum.TYPE_HANDSHAKE:
                    logger.info("HANDSHAKE start");
                    String body = sendMessage.getBody();
                    logger.info("read the HANDSHAKE message:" + body);
                    HandShakeMessage handshakeMessage = gson.fromJson(body, HandShakeMessage.class);
                    //校验握手信息
                    if (!checkHandShake(handshakeMessage.getAccount(), handshakeMessage.getPassword())) {
                        //响应握手失败
                        logger.info("handshake Fail!!");
                        Container.send(CommUtil.createHandShakeResponse(MessageEnum.status.HANDSHAKEFAIL.getCode(), uid), channel.id());
                    } else {
                        //此处判断该用户是否已经在线 如已经在线 则发送下线通知 并更新Channel容器
                        String account = handshakeMessage.getAccount();
                        if (Container.isLogin(account)) {
                            //用户已经在线 向该用户发送下线通知
                            logger.info("this account already online , going well offline it");
                            ChannelId channelId = Container.getChannelId(account);
                            Container.send(CommUtil.createPush(MessageEnum.status.OTHERLOGIN.getCode()), channelId);//发送下线通知
                        }
                        logger.info("handshake Success!!");
                        Container.addChannel(channel);
                        Container.addOrReplace(handshakeMessage.getAccount(), channel.id());
                        Container.send(CommUtil.createHandShakeResponse(MessageEnum.status.OK.getCode(), uid), channel.id());
                    }
                    break;
                case MessageEnum.TYPE_RESPONSE:
                    //收到响应  判断响应类型  消息响应和心跳响应
                    //retransConcurrentHashMap.remove(header.getUid());
                    logger.info("receive response , remove retrans task");
                    ThreadPool.removeFurure(header.getUid());
                    break;
                case MessageEnum.TYPE_PONG:
                    //心跳响应  不做任何事
                    //Container.pingPongRest(ctx.channel().id());
                    break;
                default:
                    //不支持该协议
            }
        } catch (JsonSyntaxException e) {
            //json解析异常 返回用户报错信息
            logger.error("json parse err！！");
            //Container.send(CommUtil.createResponse(MessageEnum.status.DECODEERR.getCode(),null), channel.id());//json解码错误 返回报错
            e.printStackTrace();
        } catch (NullPointerException nullpoint) {
            logger.error("null message");
            //Container.send(CommUtil.createResponse(MessageEnum.status.ERROR.getCode(),null), channel.id());//空消息
            nullpoint.printStackTrace();
        } catch (NotOnlineException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Class setType() {
        return Message.class;
    }

}
