package im.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {
    public static ApplicationContext ac = null;
    static {
    		 ac = new ClassPathXmlApplicationContext(
    						"/im/main/config/spring.xml");
    	}
}
