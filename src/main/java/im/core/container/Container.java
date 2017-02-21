package im.core.container;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;

/**
 * 用户容器 存储所有在线用户  可以抽取出来用redis等nosql实现
 * @author Misnearzhang
 *
 */
public class Container {
	private static ConcurrentHashMap<String, ChannelId> container=new ConcurrentHashMap<String, ChannelId>();
	private static EventExecutor executors=new DefaultEventExecutor();
	private static ChannelGroup group=new DefaultChannelGroup(executors);
	
	public static ChannelId getChannelId(String userName){
		return container.get(userName);
	}
	
	public static boolean addOrReplace(String userName,ChannelId channelId){
		if(container.containsKey(userName)){
			container.replace(userName, channelId);
		}else{
			container.put(userName, channelId);
		}
		return true;
	}
	
	public static boolean isLogin(String userName){
		return container.containsKey(userName);
	}

	public static boolean logOut(String userName){
		container.remove(userName);
		return true;
	}
	
	public static int getCount(){
		return container.size();
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
		group.writeAndFlush(obj, ChannelMatchers.is(channel));
	}
	
	public static void addChannel(Channel channel){
		group.add(channel);
	}
}
