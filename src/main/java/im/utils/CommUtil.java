package im.utils;

import com.google.gson.Gson;
import im.protoc.Request;
import im.protoc.pojo.RequestPOJO;

public class CommUtil {
	private static Gson gson=new Gson();
	public static Request varify(String message){
		try{
			Request request=gson.fromJson(message, Request.class);
			return request;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static RequestPOJO Protoc2POJO(Request request){
		RequestPOJO requestpojo=new RequestPOJO();
		requestpojo.setMessage_content(request.getContent());
		requestpojo.setMessage_from(request.getContent());
		requestpojo.setMessage_sign(request.getSign());
		requestpojo.setMessage_timestamp(request.getTimestamp());
		requestpojo.setMessage_to(request.getContent());
		requestpojo.setMessage_type(request.getType());
		requestpojo.setMessage_uid(request.getUid());
		return requestpojo;
	}
}
