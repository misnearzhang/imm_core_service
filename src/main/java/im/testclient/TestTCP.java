package im.testclient;

import com.google.gson.Gson;
import im.protoc.HandShakeMessage;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.MessageEnum;

import java.io.IOException;
import java.util.UUID;

public class TestTCP {
	public static void main(String[] args) throws IOException {
		Gson gson = new Gson();
/*
		Message message=new Message();
		Header header=new Header();
		header.setType("user");
		header.setStatus("100");
		header.setUid(CommUtil.createUUID());
		message.setHead(gson.toJson(header));
		UserMessage body=new UserMessage();
		body.setSign("");
		body.setType("user");
		body.setFrom("zhanglong1490457203671");
		body.setTo("zhanglong1490457737178");
		body.setContent("hellow");
		message.setBody(gson.toJson(body));
		System.out.println(gson.toJson(message));
*/

		Message message=new Message();
		Header header=new Header();
		HandShakeMessage handShakeMessage=new HandShakeMessage();
		handShakeMessage.setAccount(System.currentTimeMillis()+"");
		handShakeMessage.setPassword("123456");
		header.setUid(UUID.randomUUID().toString());
		header.setStatus("100");
		header.setType(MessageEnum.type.HANDSHAKE.getCode());
		message.setHead(header);
		message.setBody(gson.toJson(handShakeMessage));
		System.out.println(gson.toJson(message));
	}
	public byte[] getSendBlock(short length,byte[] data){
		return null;
	}
}
