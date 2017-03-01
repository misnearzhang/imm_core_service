package im.core.container;

import im.protoc.Message;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Misnearzhang on 2017/3/1.
 * when send a {@link im.protoc.Message} message ,save a copy.
 */
public class WaitQueue implements Serializable{
    private static ConcurrentLinkedQueue<Message> messages=new ConcurrentLinkedQueue<Message>();


    public boolean add(Message message){
        return messages.add(message);
    }
}
