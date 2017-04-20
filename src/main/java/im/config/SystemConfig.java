package im.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * 全局配置 包括线程池  端口等
 * Created by zhanglong on 17-3-21.
 */

/**
 * #listening port
 * tcp-port=
 * <p>
 * #heartbeat time
 * idle-read=120
 * idle-write=123
 * idle-failure-count=3
 * <p>
 * #core ThreadPool
 * threadpool-corepoolsize=
 * threadpool-maximumpoolsize=
 * #TimeUnit s
 * threadpool-keepalivetime=
 * threadpool-retransfirsttime=
 * threadpool-retranssecondtime=
 * threadpool-retransthirdtime=
 */

@Component
public class SystemConfig {
    private String delimiter;
    private int idleReadTime;
    private int idleWriteTime;
    private int tcpPort;
    private int threadCorePoolSize;
    private int threadMaximumPoolSize;
    private int threadKeepAliveTime;
    private int threadRetransFisrtTime;
    private int threadRetransSecondTime;
    private int threadRetransThirdTime;

    static {

    }
    public String getDelimiter() {
        return delimiter;
    }

    public SystemConfig setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public int getIdleReadTime() {
        return idleReadTime;
    }

    public SystemConfig setIdleReadTime(int idleReadTime) {
        this.idleReadTime = idleReadTime;
        return this;
    }

    public int getIdleWriteTime() {
        return idleWriteTime;
    }

    public SystemConfig setIdleWriteTime(int idleWriteTime) {
        this.idleWriteTime = idleWriteTime;
        return this;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public SystemConfig setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
        return this;
    }

    public int getThreadCorePoolSize() {
        return threadCorePoolSize;
    }

    public SystemConfig setThreadCorePoolSize(int threadCorePoolSize) {
        this.threadCorePoolSize = threadCorePoolSize;
        return this;
    }

    public int getThreadMaximumPoolSize() {
        return threadMaximumPoolSize;
    }

    public SystemConfig setThreadMaximumPoolSize(int threadMaximumPoolSize) {
        this.threadMaximumPoolSize = threadMaximumPoolSize;
        return this;
    }

    public int getThreadKeepAliveTime() {
        return threadKeepAliveTime;
    }

    public SystemConfig setThreadKeepAliveTime(int threadKeepAliveTime) {
        this.threadKeepAliveTime = threadKeepAliveTime;
        return this;
    }

    public int getThreadRetransFisrtTime() {
        return threadRetransFisrtTime;
    }

    public SystemConfig setThreadRetransFisrtTime(int threadRetransFisrtTime) {
        this.threadRetransFisrtTime = threadRetransFisrtTime;
        return this;
    }

    public int getThreadRetransSecondTime() {
        return threadRetransSecondTime;
    }

    public SystemConfig setThreadRetransSecondTime(int threadRetransSecondTime) {
        this.threadRetransSecondTime = threadRetransSecondTime;
        return this;
    }

    public int getThreadRetransThirdTime() {
        return threadRetransThirdTime;
    }

    public SystemConfig setThreadRetransThirdTime(int threadRetransThirdTime) {
        this.threadRetransThirdTime = threadRetransThirdTime;
        return this;
    }
}
