package im.core.executor.define;

import im.core.exception.UnSupportMessageType;
import io.netty.channel.Channel;

/**
 * define parse
 * Created by Misnearzhang on 2017/4/12.
 */
public interface Parse {

    /**
     * 获取消息类型
     *
     * @return
     */
    Class setType() throws UnSupportMessageType;


    /**
     * 解析消息
     */
    void parse(Object message, Channel channel);

}
