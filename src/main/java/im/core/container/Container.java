package im.core.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.*;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 用户容器 存储所有在线用户  可以抽取出来用redis等nosql实现
 * @author Misnearzhang
 *
 */
public class Container {
	//userAccount : ChannelId   //  用户账户  channelId
	private static ConcurrentHashMap<String, UserAccount> accountConcurrentHashMap=new ConcurrentHashMap<String, UserAccount>();
	private static ConcurrentHashMap<ChannelId, UserAccount> channelIdUserAccountConcurrentHashMap=new ConcurrentHashMap<ChannelId, UserAccount>();
	private static EventExecutor executors=new DefaultEventExecutor();
	private static ChannelGroup group=new DefaultChannelGroup(executors);
	
	public static ChannelId getChannelId(String userName){
		return accountConcurrentHashMap.get(userName).getChannelId();
	}
	
	public static boolean addOrReplace(String userName,ChannelId channelId){
		UserAccount userAccount=new UserAccount(userName,channelId);
		if(accountConcurrentHashMap.containsKey(userName)){
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

	public static boolean logOut(ChannelId channelId){
		accountConcurrentHashMap.remove(channelIdUserAccountConcurrentHashMap.get(channelId).getAccount());
		channelIdUserAccountConcurrentHashMap.remove(channelId);
		return true;
	}
	public static void pingPongCountAdd(ChannelId channelId){
		channelIdUserAccountConcurrentHashMap.get(channelId).addCount();
	}
	public static int getPingPongCount(ChannelId channelId){
		return channelIdUserAccountConcurrentHashMap.get(channelId).getHeartBeatCount();
	}
	public static int getCount(){
		return accountConcurrentHashMap.size();
	}
	
	
	public static Channel getChannel(ChannelId channelId){
		return group.find(channelId);
	}

	/**
	 * 发送信息
	 * @param obj
	 * @param channelId
	 */
	public static void send(Object obj,ChannelId channelId){
		Channel channel=group.find(channelId);
		ChannelGroupFuture futures=group.writeAndFlush(obj, ChannelMatchers.is(channel));
    futures.addListener(
        new GenericFutureListener<Future<? super Void>>() {
          public void operationComplete(Future<? super Void> future) throws Exception {
            //TODO io完成  在这里设定定时 定时(未收到消息响应则重发)
            System.out.println("所有用户Id:"+group.size());
          }
        });

	}
	public static void addChannel(Channel channel){
		group.add(channel);
	}

}
