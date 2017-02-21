package im.db.dao.impl;

import javax.annotation.Resource;

import im.db.dao.BaseDao;
import im.db.dao.IRequestDao;
import im.protoc.pojo.RequestPOJO;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RequestDao extends BaseDao implements IRequestDao {
	@Resource
	private NamedParameterJdbcTemplate jdbc;

	public NamedParameterJdbcTemplate getJdbc() {
		return jdbc;
	}

	public void setJdbc(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public void save(RequestPOJO request) {
		super.insert(jdbc, request);
	}
}
