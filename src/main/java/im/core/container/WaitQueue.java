package im.core.container;

import im.protoc.Message;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Misnearzhang on 2017/3/1.
 * when send a {@link im.protoc.Message} message ,save a copy.
 */
public class WaitQueue implements Serializable{
    /**
     * String 消息uuid
     * {@link Message} 消息正文
     */
    private static ConcurrentHashMap<String,Message> messages=new ConcurrentHashMap<String, Message>();

    public static void add(String uuid,Message message){
        messages.put(uuid,message);
    }

}
