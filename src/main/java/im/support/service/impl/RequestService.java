package im.support.service.impl;

import im.support.dao.IRequestDao;
import im.support.service.IRequestService;
import im.protoc.pojo.RequestPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("requestService")
public class RequestService implements IRequestService {
	@Autowired
	private IRequestDao requestDao;

	public boolean addRequest(RequestPOJO request) {
		requestDao.save(request);
		return true;
	}
}
