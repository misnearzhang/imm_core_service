package im.support.pool;

import im.protoc.protocolbuf.Protoc;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by misnearzhang on 2017/5/9.
 */
public class HeaderPoolFactory implements PooledObjectFactory {
    @Override
    public PooledObject makeObject() throws Exception {
        Protoc.Head.Builder message = Protoc.Head.newBuilder();
        return new DefaultPooledObject<Protoc.Head.Builder>(message);
    }

    @Override
    public void destroyObject(PooledObject pooledObject) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject pooledObject) {
        return false;
    }

    @Override
    public void activateObject(PooledObject pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject pooledObject) throws Exception {

    }
}
