package im.testclient;

import com.google.gson.Gson;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.UserMessage;
import im.utils.CommUtil;

import java.io.IOException;
import java.util.UUID;

public class TestTCP {
	public static void main(String[] args) throws IOException {
		Gson gson=new Gson();
		Message message=new Message();
		Header header=new Header();
		header.setType("user");
		header.setStatus("100");
		header.setUid(CommUtil.createUUID());
		message.setHead(gson.toJson(header));
		UserMessage body=new UserMessage();
		body.setSign("");
		body.setType("user");
		body.setFrom("zhanglong1");
		body.setTo("zhanglong1490227724257");
		body.setContent("hellow");
		message.setBody(gson.toJson(body));
		System.out.println(gson.toJson(message));
	}
	public byte[] getSendBlock(short length,byte[] data){
		return null;
	}
}
