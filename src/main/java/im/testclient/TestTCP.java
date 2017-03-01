package im.testclient;

import java.io.IOException;

public class TestTCP {
	public static void main(String[] args) throws IOException {
			/*Gson gson=new Gson();
			Socket socket=new Socket("127.0.0.1",3000);
			InputStream in=socket.getInputStream();
			OutputStream out=socket.getOutputStream();
			Message content=new Message();
			content.setContent("你好");
			content.setFrom("zhanglong1");
			content.setTimestamp(new Date().getTime());
			content.setTo("zhanglong0");
			content.setUid("12e7fc998");
			content.setType("text");
			String send=gson.toJson(content);
			System.out.println(send);
			send+="\r\n";
			out.write(send.getBytes());
			byte[] by=new byte[in.available()];
			in.read(by);
			System.out.println(new String(by));
			Scanner scan=new Scanner(System.in);
			scan.nextLine();
			out.close();
			in.close();
			socket.close();*/
			//System.out.println(new String(by));
	}
	public byte[] getSendBlock(short length,byte[] data){
		return null;
	}
}
