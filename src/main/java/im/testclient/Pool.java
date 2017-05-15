package im.testclient;

import im.protoc.protocolbuf.Protoc;
import im.support.pool.PoolUtils;

import java.util.UUID;

/**
 * Created by misnearzhang on 2017/5/9.
 */
public class Pool {
    public static void main(String[] args) {
        while(true){
            Protoc.Message.Builder builder = PoolUtils.getInstance();
            Protoc.Head.Builder head = Protoc.Head.newBuilder();
            head.setStatus(Protoc.status.DECODEERR);
            head.setTime(System.currentTimeMillis());
            head.setType(Protoc.type.HANDSHAKE);
            head.setUid(UUID.randomUUID().toString());
            builder.setHead(head);
            builder.setBody("im zhanglong");
            System.out.println(builder.build().toString());
            builder.clear();
            PoolUtils.release(builder);

        }
    }
}
