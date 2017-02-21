package im.main.config;

import im.db.SpringContext;
import im.db.service.IRequestService;
import im.protoc.Request;
import im.utils.CommUtil;

import java.util.Date;


public class Main {
	public static void main(String[] args) {
		IRequestService requestService=(IRequestService) SpringContext.ac.getBean("requestService");
		Request request=new Request();
		request.setContent("uysd");
		request.setUid("4567890-");
		request.setFrom("1065302407");
		request.setTimestamp(new Date().getTime());
		request.setTo("10390756891");
		request.setType("1");
		request.setSign("dfghjk");
		requestService.addRequest(CommUtil.Protoc2POJO(request));
		System.out.println("");
	}
}
