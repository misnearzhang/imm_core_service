package im.db.service.impl;

import im.db.dao.IRequestDao;
import im.db.service.IRequestService;
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
