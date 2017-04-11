package im.utils;

import com.google.gson.Gson;
import im.main.handler.WorkerInBoundHandler;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.MessageEnum;
import im.protoc.pojo.RequestPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class CommUtil {
    private final Logger logger = LogManager.getLogger(CommUtil.class);
    private static Gson gson = new Gson();

    public static Message varify(String message) {
        Message request = gson.fromJson(message, Message.class);
        return request;
    }

    public static RequestPOJO Protoc2POJO(Message message) {
        RequestPOJO requestpojo = new RequestPOJO();
        /*requestpojo.setMessage_content(message.getContent());
        requestpojo.setMessage_from(message.getContent());
		requestpojo.setMessage_sign(message.getSign());
		requestpojo.setMessage_timestamp(message.getTimestamp());
		requestpojo.setMessage_to(message.getContent());
		requestpojo.setMessage_type(message.getType());
		requestpojo.setMessage_uid(message.getUid());*/
        return requestpojo;
    }

    public static String createHeartBeatMessage() {
        StringBuilder sb = new StringBuilder("");
        Message message = new Message();
        Header head = new Header();
        head.setUid(UUID.randomUUID().toString());
        head.setType(MessageEnum.type.PING.getCode());
        head.setStatus(MessageEnum.status.REQ.getCode());
        message.setHead(head);
        sb.append(gson.toJson(message)).append(MessageEnum.delimiters.ENTER.getCode());
        return sb.toString();
    }

    public static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String createResponse(String status, String uuid) {
        Message response = new Message();
        Header header1 = new Header();
        header1.setUid(uuid);
        header1.setStatus(status);
        header1.setType(MessageEnum.type.RESPONSE.getCode());
        response.setHead(header1);
        String send = gson.toJson(response);
        send += "\r\n";
        return send;
    }

    public static String createHandShakeResponse(String status, String uuid) {
        Message response = new Message();
        Header header1 = new Header();
        header1.setUid(uuid);
        header1.setStatus(status);
        header1.setType(MessageEnum.type.HANDSHAKERESPONSE.getCode());
        response.setHead(header1);
        String send = gson.toJson(response);
        send += "\r\n";
        return send;
    }

    public static String createPush() {
        Message message = new Message();
        Header header = new Header();
        header.setType(MessageEnum.type.SYSTEM.getCode());
        header.setStatus(MessageEnum.status.OTHERLOGIN.getCode());
        header.setUid(UUID.randomUUID().toString());
        message.setHead(header);
        String send = gson.toJson(message + "\r\n");
        return send;
    }
}
