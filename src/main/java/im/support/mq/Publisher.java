package im.support.mq;

import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

/**
 * Created by Misnearzhang on 2017/3/6.
 */

@Service("publisher")
public class Publisher extends EndPoint {
    public Publisher(String endpointName) throws IOException, TimeoutException {
        super(endpointName);
    }

    public  void sendMessage(Serializable object) throws IOException {
        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
    }
}
