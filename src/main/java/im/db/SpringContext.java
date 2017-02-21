package im.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {
    public static ApplicationContext ac = null;
    static {
    		 ac = new ClassPathXmlApplicationContext(
    						"com/im/main/config/spring.xml");
    	}
}
