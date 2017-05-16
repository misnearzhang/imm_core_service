package im.core.define;

import im.core.exception.UnSupportMessageType;
import im.protoc.protocolbuf.Protoc;
import io.netty.channel.Channel;

/**
 * define parse
 * Created by Misnearzhang on 2017/4/12.
 */
public interface Parse {

    /**
     * 解析消息
     */
    void parse(Protoc.Message message , Channel channel);
}
