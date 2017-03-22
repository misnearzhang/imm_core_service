package im.core.container;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import im.config.SystemConfig;
import im.core.executor.RetransTask;
import im.core.executor.ThreadPool;
import im.main.handler.WorkerInBoundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.*;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 用户容器 存储所有在线用户  可以抽取出来用redis等nosql实现
 * @author Misnearzhang
 *
 */
public class Container {
	private final Logger logger = LogManager.getLogger( Container.class );

	//userAccount : ChannelId   //  用户账户  channelId
	private static ConcurrentHashMap<String, UserAccount> accountConcurrentHashMap=new ConcurrentHashMap<String, UserAccount>(100000);
	private static ConcurrentHashMap<ChannelId, UserAccount> channelIdUserAccountConcurrentHashMap=new ConcurrentHashMap<ChannelId, UserAccount>(100000);

	private static EventExecutor executors=new DefaultEventExecutor();//channel使用的线程池
	private static ChannelGroup group=new DefaultChannelGroup(executors);//维护 所有在线的channel

	/**
	 * 根据userAccount 获取channelId
	 * @param userAccount
	 * @return
	 */
	public static ChannelId getChannelId(String userAccount){
		if(accountConcurrentHashMap.contains(userAccount)){
			return accountConcurrentHashMap.get(userAccount).getChannelId();
		}else{
			return null;
		}
	}
	public static Channel getChannel(ChannelId channelId){
		return group.find(channelId);
	}

	/**
	 * 保存用户相关的 一些数据
	 * @param userName
	 * @param channelId
	 * @return
	 */
	public static boolean addOrReplace(String userName,ChannelId channelId){
		UserAccount userAccount=new UserAccount(userName,channelId);
		if(accountConcurrentHashMap.containsKey(userName)&&channelIdUserAccountConcurrentHashMap.contains(channelId)){
			accountConcurrentHashMap.replace(userName, userAccount);
			channelIdUserAccountConcurrentHashMap.replace(channelId,userAccount);
		}else{
			accountConcurrentHashMap.put(userName, userAccount);
			channelIdUserAccountConcurrentHashMap.put(channelId,userAccount);
		}
		return true;
	}

	public static boolean isLogin(String userName){
		return accountConcurrentHashMap.containsKey(userName);
	}

	/**
	 * 登出 清理 accountConcurrentHashMap 和 channelIdUserAccountConcurrentHashMap中数据
	 * @param channelId
	 * @return
	 */
	public static boolean logOut(ChannelId channelId){
		accountConcurrentHashMap.remove(channelIdUserAccountConcurrentHashMap.get(channelId).getAccount());
		channelIdUserAccountConcurrentHashMap.remove(channelId);
		return true;
	}

	//将用户心跳标识加1
	public static void pingPongCountAdd(ChannelId channelId){
		channelIdUserAccountConcurrentHashMap.get(channelId).addCount();
	}

	//获得心跳数
	public static int getPingPongCount(ChannelId channelId){
		return channelIdUserAccountConcurrentHashMap.get(channelId).getHeartBeatCount();
	}

	//用户在线数量
	public static int getCount(){
		return group.size();
	}
	

	/**
	 * 发送信息
	 * @param obj
	 * @param channelId
	 */
	public static void send(final Object obj, ChannelId channelId){
		Channel channel=group.find(channelId);
		ChannelGroupFuture futures=group.writeAndFlush(obj, ChannelMatchers.is(channel));
		futures.addListener(
        new GenericFutureListener<Future<? super Void>>() {
          public void operationComplete(Future<? super Void> future) throws Exception {
			  ThreadPool.retransExecutor.schedule(new RetransTask(null, ThreadPool.RetransCount.FISRT), SystemConfig.threadRetransFisrtTime, TimeUnit.SECONDS);
          }
        });

	}
	public static void sendHeartBeat(final Object obj,ChannelId channelId){
		Channel channel=group.find(channelId);
		group.writeAndFlush(obj,ChannelMatchers.is(channel));
	}
	public static void addChannel(Channel channel){
		group.add(channel);
	}

}
