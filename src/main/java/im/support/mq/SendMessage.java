package im.support.mq;

import im.protoc.db.OfflineMessage;

/**
 * Created by Misnearzhang on 2017/4/14.
 */
public interface SendMessage {
    void send2db(OfflineMessage message);
}
