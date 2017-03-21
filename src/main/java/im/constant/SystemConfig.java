package im.constant;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 全局配置 包括线程池  端口等
 * Created by zhanglong on 17-3-21.
 */
public class SystemConfig {
    static{
        Map map=readFile("/systemconfig");
    }


    static Map readFile(String path){
        //载入配置文件
        Map<String,String > map=new HashMap<String, String>();
        Properties pps = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            pps.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Enumeration en = pps.propertyNames(); //得到配置文件的名字

        while(en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
            map.put(strKey,strValue);
        }
        return map;
    }

}
