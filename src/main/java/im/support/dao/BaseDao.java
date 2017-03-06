package im.support.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.utils.DBUtil;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

public class BaseDao  {
	
	private static final int DEFAULT_ROW_INDEX = 0;
	private static final int DEFAULT_ROW_SIZE = 100;
	
	
	public <T> int insert( NamedParameterJdbcOperations hnaJdbc, T t) {
		String table =  getTableName(t.getClass());
		String sql = DBUtil.generateInsertSQL(table, t);
		return hnaJdbc.update(sql, new BeanPropertySqlParameterSource(t));
	}
	public <T> int insertFetchId(NamedParameterJdbcOperations hnaJdbc,T t) {
		String table = getTableName(t.getClass());
		String sql = DBUtil.generateInsertSQL(table, t);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		hnaJdbc.update(sql, new BeanPropertySqlParameterSource(t), keyHolder);
		return keyHolder.getKey().intValue();
	}
	

	public <T> int update(NamedParameterJdbcOperations hnaJdbc,T t,Map<String,Object> conditions) {
		Map<String, Object> fields = DBUtil.comparePojo(null, t);
		fields.remove("updateTime");
		if (fields == null || fields.size() == 0) {
			return 0;
		}
		
		String table  = getTableName(t.getClass());
		String sql = DBUtil.generateUpdateSQL(table, fields, conditions);
		return hnaJdbc.update(sql, new BeanPropertySqlParameterSource(t));
	}

	
	public <T> int update(NamedParameterJdbcOperations hnaJdbc,T t, Map<String, Object> fileds,
			Map<String, Object> conditions) {
		String conditionsExt = " limit 100 ";
		String table = getTableName(t.getClass());
		String sql = DBUtil.generateUpdateSQL(table, fileds, conditions, conditionsExt);
		
		fileds.putAll(conditions);
		DBUtil.format(fileds);
		
		return hnaJdbc.update(sql, fileds);
	}


	public <T> int delete(NamedParameterJdbcOperations hnaJdbc,T t,Map<String,Object> conditions) {
		Map<String, Object> fields = DBUtil.comparePojo(null, t);
		fields.remove("updateTime");
		if (fields == null || fields.size() == 0) {
			return 0;
		}
		
		String table  = getTableName(t.getClass());
		String sql = DBUtil.generateDeleteSQL(table, conditions);
		return hnaJdbc.update(sql, new BeanPropertySqlParameterSource(t));
	}

	public <T> int replace(NamedParameterJdbcOperations hnaJdbc,T t) {
		String table =  getTableName(t.getClass());
		String sql = DBUtil.generateReplaceSQL(table, t);
		return hnaJdbc.update(sql, new BeanPropertySqlParameterSource(t));
	}

	public <T> List<Map<String, Object>> listMapByFields(NamedParameterJdbcOperations hnaJdbc,
			Map<String, Object> fields, Class<T> clazz, int rowIndex,
			int rowSize,String orderBy) {
		String conditionsExt = "limit :rowIndex, :rowSize";
		if(orderBy!=null&&!"".equals(orderBy)){
			conditionsExt = orderBy +" "+ conditionsExt ;
		}
		
		String table = getTableName(clazz);
		String sql = DBUtil.generateSelectSQL(table, fields, conditionsExt);
		
		Map<String, Object> params = new HashMap<String, Object>(fields);
		params.put("rowIndex", rowIndex);
		params.put("rowSize", rowSize);
		return hnaJdbc.queryForList(sql, params);
	}
	
	public <T> List<Map<String, Object>> listMapByFields(NamedParameterJdbcOperations hnaJdbc,
			Map<String, Object> fields, Class<T> clazz, String orderBy) {
		
		String table = getTableName(clazz);
		String sql = DBUtil.generateSelectSQL(table, fields, orderBy);
		
		return hnaJdbc.queryForList(sql, fields);
	}
	
	 protected String getTableName(Object obj)
	  {
	    if (obj == null) return null;
	    String table = "";
	    if (StringUtils.isEmpty(table)) {
	      if (obj.getClass().equals(String.class))
	        table = obj.toString();
	      else if (Class.class.equals(obj.getClass()))
	        table = ((Class)obj).getSimpleName();
	      else {
	    	  table=obj.getClass().getSimpleName();
	    	  return (new StringBuilder()).append(table.toLowerCase()).toString();
	      }
	    }
	    return (new StringBuilder()).append(table.toLowerCase()).toString();
	  }
	 

}
