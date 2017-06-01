package im.support.pool;

import im.protoc.protocolbuf.Protoc;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by misnearzhang on 2017/5/9.
 */
public class PoolUtils {

    private static GenericObjectPool<Protoc.Message.Builder> pool;
    private static GenericObjectPool<Protoc.Head.Builder> head_pool;
    static {
        MessagePoolFactory factory = new MessagePoolFactory();
        //资源池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(2);
        //创建资源池
        pool= new GenericObjectPool<Protoc.Message.Builder>(factory,poolConfig);
        head_pool = new GenericObjectPool<Protoc.Head.Builder>(factory,poolConfig);
    }
    public static Protoc.Message.Builder getMessageInstance() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Protoc.Head.Builder getHeaderInstance() {
        try {
            return head_pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void releaseMessage(Protoc.Message.Builder message){
        pool.returnObject(message);
    }
    public static void releaseHeader(Protoc.Head.Builder header){
        head_pool.returnObject(header);
    }

}
