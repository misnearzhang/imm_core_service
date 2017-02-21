package im.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *
 */
public class DBUtil {
	
	private static Log log = LogFactory.getLog(DBUtil.class);
	
	private final static int N = 100;
	private final static String ZERO = "0";
	private final static String SEPARATOR = "_";
	private final static String EMPTY = "";
	private final static String BLANK = " ";
	private final static String COMMA = ",";
	private final static String  SELECT = "select";
	private final static String  FROM = "from";
	private final static int MAXNUM = 100000;
	
	/**
	 * ��ȡɢ�б�
	 * @param table ��������
	 * @param flag	ɢ������
	 * @param n		ɢ�д�С
	 * @return
	 */
	public static String getHashTable(String table, long flag, int n) {
		if (flag < 0 || n <= 0) {
			return table;
		}
		
		long suffix = flag % n;
		
		int currentSuffixLength = Long.toString(suffix).length();
		int completeSuffixLength = Integer.toString(n).length();		
		if (Integer.parseInt(Integer.toString(n).substring(0, 1)) == 1 && Integer.parseInt(Integer.toString(n).substring(1)) == 0) {
			completeSuffixLength--;
		}
		
		int lackSuffixLength = completeSuffixLength - currentSuffixLength;
		
		StringBuilder sb = new StringBuilder().append(table).append(SEPARATOR);
		for (int i = 0; i < lackSuffixLength; i++) sb.append(ZERO);
		sb.append(suffix);
		
		return sb.toString();
	}
	
	/**
	 * ��ȡɢ�б�
	 * @param table	��������
	 * @param flag	ɢ������
	 * @return
	 */
	public static String getHashTable(String table, long flag) {
		return getHashTable(table, flag, N);
	}
	
	public static String getCountSql(String sql){
		sql = sql.replaceAll("(^\\s*\\()|(\\)\\s*$)", "")
				 .replaceAll("(?i)select\\s+", "select ")
				 .replaceAll("(?i)from\\s+", "from ")
				 .replaceAll("(?i)order\\s+by", "order by")
				 .replaceAll("(?i)limit", "limit");
		int pointer = sql.indexOf(SELECT, 0);
		//���﷨����ջ
		LinkedList<String> stack  =  new LinkedList<String>();
		stack.push(SELECT);
		while(!stack.isEmpty()){
			String first = stack.getFirst();
			int p1 = sql.indexOf(SELECT, pointer + 4);
			int p2 = sql.indexOf(FROM, pointer + 4);
			p1 = (p1 == -1)? MAXNUM: p1;
			p2 = (p2 == -1)? MAXNUM: p2;
			if(p1 == MAXNUM && p2 == MAXNUM) throw new RuntimeException("SQL���Ƿ�...");
			if(p1 <= p2){
				pointer = p1;
				if(first.equals(SELECT))stack.push(SELECT);
				else stack.pop();
			}else{
				pointer = p2;
				if(first.equals(FROM))stack.push(FROM);
				else stack.pop();
			}
		}		
		int fromIndex = pointer;
		sql = sql.substring(fromIndex);
		int orderIndex = sql.lastIndexOf("order by");
		if(orderIndex != -1 && sql.indexOf(")", orderIndex) < 0){
			sql = sql.substring(0, orderIndex);
		}
		int limitIndex = sql.lastIndexOf("limit");
		if(limitIndex != -1 && sql.indexOf(")", limitIndex) < 0){
			sql = sql.substring(0, limitIndex);
		}
		return "select count(1) count " + sql;
	}
	
	/**
	 * Format fields
	 * @param fields
	 */
	public static void format(Map<String, Object> fields) {
		for (String key : fields.keySet()) {
			if (fields.get(key) instanceof Boolean) {
				fields.put(key, (Boolean)fields.get(key) ? 1 : 0);
			}
		}
	}
	
	/**
	 * Select sql restrict
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public static String restrictSelectSQL(String sql, Class<?> clazz) {
		return sql.replace("*", generateTargetFields(clazz));
	}
	
	/**
	 * Generate select sql statement
	 * @param table
	 * @param conditions
	 * @param conditionsExt
	 * @return
	 */
	public static String generateSelectSQL(String table, Map<String, Object> conditions, String conditionsExt) {
		StringBuilder sb = new StringBuilder().append("select * from ").append(table).append(" where 1 = 1");
		if (conditions == null || conditions.isEmpty()) {
			sb.append(" ").append(conditionsExt);
			return sb.toString();
		}
		
		
		for (Iterator<String> it = conditions.keySet().iterator(); it.hasNext(); ) {
			String key = it.next();
			sb.append(" and ").append(key).append(" = ").append(":").append(key);
		}
		
		if (StringUtils.isNotEmpty(conditionsExt)) {
			sb.append(" ").append(conditionsExt);
		}
		
		return sb.toString();
	}
	
	/**
	 * Generate select sql statement
	 * @param table
	 * @param conditions
	 * @return
	 */
	public static String generateSelectSQL(String table, Map<String, Object> conditions) {
		return generateSelectSQL(table, conditions, "");
	}
	
	/**
	 * Generate insert sql statement
	 * @param <T>
	 * @param table
	 * @param t
	 * @return
	 */
	public static <T> String generateInsertSQL(String table, T t) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append("insert into ").append(table).append(" (");
		sb2.append(") values (");
		
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) continue;
			
			sb1.append(field.getName()).append(", ");
			sb2.append(":").append(field.getName()).append(", ");
		}
		sb1.delete(sb1.lastIndexOf(","), sb1.length());
		sb2.delete(sb2.lastIndexOf(","), sb2.length());
		sb1.append(sb2).append(")");
		
		return sb1.toString();
	}
	
	/**
	 * Generate insert sql statement
	 * @param table
	 * @param fields
	 * @return
	 */
	public static String generateInsertSQL(String table, Map<String, Object> fields) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append("insert into ").append(table).append(" (");
		sb2.append(" ) values (");
		
		for (Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			sb1.append(key);
			sb2.append(":").append(key);
			
			if (it.hasNext()) {
				sb1.append(", ");
				sb2.append(", ");
			}
		}
		
		sb1.append(sb2).append(")");
		
		return sb1.toString();
	}
	
	/**
	 * Generate reaplce sql statement
	 * @param <T>
	 * @param table
	 * @param t
	 * @return
	 */
	public static <T> String generateReplaceSQL(String table, T t) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append("replace into ").append(table).append(" (");
		sb2.append(") values (");
		
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) continue;
			
			sb1.append(field.getName()).append(", ");
			sb2.append(":").append(field.getName()).append(", ");
		}
		sb1.delete(sb1.lastIndexOf(","), sb1.length());
		sb2.delete(sb2.lastIndexOf(","), sb2.length());
		sb1.append(sb2).append(")");
		
		return sb1.toString();
	}
	
	/**
	 * Generate reaplce sql statement
	 * @param table
	 * @param fields
	 * @return
	 */
	public static String generateReplaceSQL(String table, Map<String, Object> fields) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append("replace into ").append(table).append(" (");
		sb2.append(") values (");
		
		for (Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			sb1.append(key);
			sb2.append(":").append(key);
			
			if (it.hasNext()) {
				sb1.append(", ");
				sb2.append(", ");
			}
		}
		
		sb1.append(sb2).append(")");
		
		return sb1.toString();
	}
	
	public static String generateUpdateSQL(String table, Map<String, Object> fields, Map<String, Object> conditions) {
		return generateUpdateSQL(table, fields, conditions, "");
	}
	
	/**
	 * Generate update sql statement
	 * @param table
	 * @param fields
	 * conditions Map<String, Object> conditions
	 * @return
	 */
	public static String generateUpdateSQL(String table, Map<String, Object> fields, Map<String, Object> conditions, String conditionsExt) {
		if (fields == null || fields.size() == 0) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder().append("update ").append(table).append(" set ");
		for (Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			String key = it.next();
			sb.append(key).append(" = ").append(":").append(key);
			if (it.hasNext()) {
				sb.append(", ");
			}	
		}
		sb.append(" where 1 = 1");
		for (Iterator<String> it = conditions.keySet().iterator(); it.hasNext(); ) {
			String key = it.next();
			sb.append(" and ").append(key).append(" = ").append(":").append(key);
		}
		
		if (!StringUtils.isEmpty(conditionsExt)) {
			sb.append(" ").append(conditionsExt);
		}
		
		return sb.toString();
	}
	
	/**
	 * Generate delete sql statement
	 * @param table
	 * @param conditions
	 * @return
	 */
	public static String generateDeleteSQL(String table, Map<String, Object> conditions) {
		
		return generateDeleteSQL(table, conditions, "");
	}
	
	public static String generateDeleteSQL(String table, Map<String, Object> conditions, String conditionsExt) {
		if (conditions == null || conditions.size() == 0) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder().append("delete from ").append(table).append(" where 1 = 1");
		for (Iterator<String> it = conditions.keySet().iterator(); it.hasNext(); ) {
			String key = it.next();
			sb.append(" and ").append(key).append(" = ").append(":").append(key);
		}
		
		if (!StringUtils.isEmpty(conditionsExt)) {
			sb.append(" ").append(conditionsExt);
		}
		
		return sb.toString();
	}
	
	/**
	 * Generate count sql statement
	 * @param table
	 * @param conditions
	 * @return
	 */
	public static String generateCountSQL(String table, Map<String, Object> conditions) {
		StringBuilder sb = new StringBuilder().append("select count(1) from ").append(table).append(" where 1 = 1");
		
		if (conditions != null && conditions.size() != 0) {
			for (Iterator<String> it = conditions.keySet().iterator(); it.hasNext(); ) {
				String key = it.next();
				sb.append(" and ").append(key).append(" = ").append(":").append(key);
			}
		}
		//sb.append(" limit 1");		
		
		return sb.toString();
	}
	
	
	/**
	 * Generate count sql statement
	 * @param table
	 * @param conditions
	 * @return
	 */
	public static String generateSumSQL(String table, Map<String, Object> conditions) {
		StringBuilder sb = new StringBuilder().append("select sum(amt) from ").append(table).append(" where 1 = 1");
		
		if (conditions != null && conditions.size() != 0) {
			for (Iterator<String> it = conditions.keySet().iterator(); it.hasNext(); ) {
				String key = it.next();
				sb.append(" and ").append(key).append(" = ").append(":").append(key);
			}
		}
		sb.append(" limit 1");		
		
		return sb.toString();
	}
	
	
	/**
	 * Generate count sql statement
	 * @param table
	 * @param conditions
	 * @return
	 */
	public static String generateCountSQL(String table, Map<String, Object> conditions ,String conditionsExt) {
		StringBuilder sb = new StringBuilder().append("select count(1) from ").append(table).append(" where 1 = 1");
		
		if (conditions != null && conditions.size() != 0) {
			for (Iterator<String> it = conditions.keySet().iterator(); it.hasNext(); ) {
				String key = it.next();
				sb.append(" and ").append(key).append(" = ").append(":").append(key);
			}
		}
		
		sb.append("  "+conditionsExt+" limit 1");		
		
		return sb.toString();
	}
	/**
	 * ���ɲ�ѯ����
	 * @param conditions
	 * @return
	 */
	public static String generateClause(Map<String, Object> conditions){
		StringBuilder sb = new StringBuilder();
		if (conditions != null && conditions.size() != 0) {
			for (Iterator<String> it = conditions.keySet().iterator(); it.hasNext(); ) {
				String key = it.next();
				sb.append(" and ").append(key).append(" = ").append(":").append(key);
			}
		}
		return sb.toString();
	}
	/**
	 * Generate target string
	 * @param clazz
	 * @return
	 */
	public static String generateTargetFields(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) continue;
			
			sb.append(field.getName() + ", ");
		}
		sb.delete(sb.lastIndexOf(","), sb.length());
		
		return sb.toString();
	}
	
	/**
	 * Compare object
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static Map<String, Object> comparePojo(Object src, Object dest) {
		if (dest == null || (src != null && !dest.getClass().equals(src.getClass()))) {
			return null;
		}
		
		Map<String, Object> values = new HashMap<String, Object>(); 
		Field[] fields = dest.getClass().getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			Object value = null;
			
			if ("serialVersionUID".equals(name)) continue; 
			Object v1 = null;
			Object v2 = null;
			
			try {
				String methodPrefix = boolean.class.equals(field.getType()) ? "is" : "get";
				Method m = dest.getClass().getMethod(new StringBuilder(methodPrefix).append(getFirstUpperString(name)).toString(), null);
				v1 = m.invoke(dest, null);
				if (src != null) {
					v2 = m.invoke(src, null);
				}
			} catch (Exception e) {
				log.error(e + " : name=" + name + ", value=" + value, e);
			}
			if (v1 != null && !v1.equals(v2)) {
				values.put(name, v1);
			}
		}
		
		return values;
	}
	
	public static String getFirstUpperString(String s) {
		if (s == null || s.length() < 1) {
			return EMPTY;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
}
