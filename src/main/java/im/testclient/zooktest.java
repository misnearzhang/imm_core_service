package im.testclient;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by misnearzhang on 5/17/17.
 */
public class zooktest implements Runnable {
    private static ZooKeeper zooKeeper;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        connect();
        countDownLatch.await();
        //zooKeeper.create("/nana", UUID.randomUUID().toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        Thread thread = new Thread(new zooktest());
        thread.start();
    }

    public static void connect() throws IOException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 1000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("connect");
            }
        });
        if (zooKeeper != null) {
            countDownLatch.countDown();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                zooKeeper.exists("/zhanglong", watchedEvent -> {
                    switch (watchedEvent.getType()) {
                        case NodeCreated:
                            break;
                        case NodeDataChanged:
                            System.out.println(new String(watchedEvent.getPath()));
                            try {
                                System.out.println(new String(zooKeeper.getData(watchedEvent.getPath(), false, null)));
                            } catch (KeeperException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case NodeChildrenChanged:
                            try {
                                List<String> children = zooKeeper.getChildren(watchedEvent.getPath(), true);
                                children.forEach(k -> {
                                    try {
                                        byte[] s = zooKeeper.getData("/" + k, true, null);
                                        System.out.println(new String(s));
                                    } catch (KeeperException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } catch (KeeperException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                            default:
                                System.out.println("default");
                    }
                });
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
