package im.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringBeanUtil implements BeanFactoryAware {

	private static BeanFactory beanFactory = null;  
    private static SpringBeanUtil factoryUtils = null;  
    
    
    
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {  
    	SpringBeanUtil.beanFactory = beanFactory;  
    }  
    
    public static BeanFactory getBeanFactory() {  
        return beanFactory;  
    }
    
    public static SpringBeanUtil getInstance(){  
        if(factoryUtils==null){  
            //factoryUtils = (SpringBeanFactoryUtils)beanFactory.getBean("springBeanFactoryUtils");  
            factoryUtils = new SpringBeanUtil();  
        }  
        return factoryUtils;
    }
    
  
    public static Object getBean(String name){  
        return beanFactory.getBean(name);  
    }

    
    public static <T> T getBean(Class<?> clazz){  
    	return (T) beanFactory.getBean(clazz);  
    }  
}
