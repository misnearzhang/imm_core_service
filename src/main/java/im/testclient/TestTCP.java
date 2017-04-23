package im.testclient;

import com.google.gson.Gson;
import im.protoc.HandShakeMessage;
import im.protoc.Header;
import im.protoc.Message;
import im.protoc.MessageEnum;
import im.protoc.protocolbuf.Protoc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class TestTCP {
	public static void main(String[] args) throws IOException, InterruptedException {
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

		Socket socket = new Socket("127.0.0.1",3000);
		OutputStream outputStream;
		InputStream inputStream;
		outputStream = socket.getOutputStream();
		inputStream = socket.getInputStream();
		Protoc.Message.Builder Proto = Protoc.Message.newBuilder();
		Protoc.Message.Head.Builder Head = Protoc.Message.Head.newBuilder();
		Head.setType(Protoc.Message.type.HANDSHAKE);
		Head.setStatus(Protoc.Message.status.REQ);
		Head.setUid(UUID.randomUUID().toString());
		Proto.setHead(Head);
		HandShakeMessage handShakeMessage1=new HandShakeMessage();
		handShakeMessage1.setAccount("1065302407");
		handShakeMessage1.setPassword("123456");
		Proto.setBody(gson.toJson(handShakeMessage));
		outputStream.write(Proto.build().toByteArray());
		while(true) {
			if (inputStream.available() > 0) {
				byte[] read = new byte[inputStream.available()];
				inputStream.read(read);
				System.out.println(read);
			}else Thread.sleep(1000);
		}
	}
	public byte[] getSendBlock(short length,byte[] data){
		return null;
	}
}
