package im.testclient;

import com.google.gson.Gson;
import im.protoc.Message;

import java.io.IOException;

public class TestTCP {
	public static void main(String[] args) throws IOException {
		Message message=new Message();
		message.setHead("test");
		message.setBody("one");
		Gson gson=new Gson();
		System.out.println(gson.toJson(message));
	}
	public byte[] getSendBlock(short length,byte[] data){
		return null;
	}
}
