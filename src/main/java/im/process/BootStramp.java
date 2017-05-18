package im.process;

import im.config.SystemConfig;
import im.core.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by misnearzhang on 5/16/17.
 */
public class BootStramp {
    private static SystemConfig systemConfig;
    public static void main(String[] args) {
        try {
            Server server=new Server();
            ApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
            systemConfig = springContext.getBean(SystemConfig.class);
            ThreadPool threadPool = springContext.getBean(ThreadPool.class);
            threadPool.reflectParse(ParseTask.class);
            server.setThreadPool(threadPool);
            server.bind(systemConfig.getTcpPort(),systemConfig.getIdleReadTime(),systemConfig.getIdleWriteTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
