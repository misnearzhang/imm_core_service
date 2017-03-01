package im.utils;

import com.google.gson.Gson;
import im.protoc.Message;
import im.protoc.pojo.RequestPOJO;

public class CommUtil {
	private static Gson gson=new Gson();
	public static Message varify(String message){
		try{
			Message request=gson.fromJson(message, Message.class);
			return request;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static RequestPOJO Protoc2POJO(Message message){
		RequestPOJO requestpojo=new RequestPOJO();
		requestpojo.setMessage_content(message.getContent());
		requestpojo.setMessage_from(message.getContent());
		requestpojo.setMessage_sign(message.getSign());
		requestpojo.setMessage_timestamp(message.getTimestamp());
		requestpojo.setMessage_to(message.getContent());
		requestpojo.setMessage_type(message.getType());
		requestpojo.setMessage_uid(message.getUid());
		return requestpojo;
	}
}
