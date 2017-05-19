package im.support.pool;

import im.protoc.protocolbuf.Protoc;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by misnearzhang on 2017/5/9.
 */
public class PoolUtils {

    private static GenericObjectPool<Protoc.Message.Builder> pool;
    static {
        MessagePoolFactory factory = new MessagePoolFactory();
        //资源池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(2);
        //创建资源池
        pool= new GenericObjectPool<Protoc.Message.Builder>(factory,poolConfig);
    }
    public static Protoc.Message.Builder getInstance() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void release(Protoc.Message.Builder message){
        pool.returnObject(message);
    }

}
