package im.support.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * upgrade
 * Created by Misnearzhang on 2017/3/6.
 */

@Component
public class Publisher {
    private static final Object LOCK = new Object();

    private final static String QUEUE_NAME = "queue-from-core-server";
    private final static String EXCHANGE_NAME = "exchange-from-core-server";
    private final static String ROUTINGKEY = "1111111111";
    private final static String HOST = "localhost";
    private final static String USERNAME = "zhanglong";
    private final static String PASSWORD = "123456";

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;

    private static Publisher publisher;

    public static Publisher newInstance() {
        if (publisher == null) {
            synchronized (LOCK) {
                if (publisher == null) {
                    return new Publisher();
                } else {
                    return publisher;
                }
            }
        } else {
            return publisher;
        }
    }

    private Publisher() {
        try {
            init();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTINGKEY);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    private void init() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        connection = factory.newConnection();
    }

    public void send(String message) throws IOException, TimeoutException {
        channel = connection.createChannel();
        channel.basicPublish("exchange-from-core-server", "1111111111", null, message.getBytes());
    }

    public void close() throws IOException, TimeoutException {
        if (channel.isOpen()) {
            channel.close();
        }
        if (connection.isOpen()) {
            connection.close();
        }
    }
}
