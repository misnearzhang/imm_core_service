package im.config;

import java.io.*;
import java.util.*;

/**
 * 全局配置 包括线程池  端口等
 * Created by zhanglong on 17-3-21.
 */

/**
 * #listening port
 tcp-port=

 #heartbeat time
 idle-read=120
 idle-write=123
 idle-failure-count=3

 #core ThreadPool
 threadpool-corepoolsize=
 threadpool-maximumpoolsize=
 #TimeUnit s
 threadpool-keepalivetime=
 threadpool-retransfirsttime=
 threadpool-retranssecondtime=
 threadpool-retransthirdtime=
 */
public class SystemConfig {

/*    public int idleReadTime;
    public int idleWriteTime;
    public int tcpPort;
    public int threadCorePoolSize;
    public int threadMaximumPoolSize;
    public int threadKeepAliveTime;
    public int threadRetransFisrtTime;
    public int threadRetransSecondTime;
    public int threadRetransThirdTime;*/

    private static final Map<String, String> map=readFile("/home/zhanglong/IdeaProjects/imm_core_service/src/main/resources/systemconfig");;

    public static final int idleReadTime;

    public static final int idleWriteTime;

    public static final int tcpPort;

    public static final int threadCorePoolSize;

    public static final int threadMaximumPoolSize;

    public static final int threadKeepAliveTime;

    public static final int threadRetransFisrtTime;

    public static final int threadRetransSecondTime;

    public static final int threadRetransThirdTime;

    static {
        idleReadTime= Integer.parseInt(map.get("idle-read"));
        idleWriteTime= Integer.parseInt(map.get("idle-write"));
        tcpPort= Integer.parseInt(map.get("tcp-port"));
        threadCorePoolSize= Integer.parseInt(map.get("threadpool-corepoolsize"));
        threadMaximumPoolSize= Integer.parseInt(map.get("threadpool-maximumpoolsize"));
        threadKeepAliveTime= Integer.parseInt(map.get("threadpool-keepalivetime"));
        threadRetransFisrtTime= Integer.parseInt(map.get("threadpool-retransfirsttime"));
        threadRetransSecondTime= Integer.parseInt(map.get("threadpool-retranssecondtime"));
        threadRetransThirdTime= Integer.parseInt(map.get("threadpool-retransthirdtime"));
    }


    static Map<String,String> readFile(String path){
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
