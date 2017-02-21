package im.protoc.pojo;

import java.io.Serializable;

/**
 * 数据库POJO
 * @author Misnearzhang
 *
 */
public class RequestPOJO implements Serializable{
	private static final long serialVersionUID = 7336174291983438730L;
	private String message_uid;//UUID  128bit 
	private String message_from;// system friend group 
	private String message_to;//	system friend broadcast
	private String message_type;//sys user 
	private String message_content;//加密字符串
	private Long message_timestamp;
	private String message_sign;//校验和
	public String getMessage_uid() {
		return message_uid;
	}
	public void setMessage_uid(String message_uid) {
		this.message_uid = message_uid;
	}
	public String getMessage_from() {
		return message_from;
	}
	public void setMessage_from(String message_from) {
		this.message_from = message_from;
	}
	public String getMessage_to() {
		return message_to;
	}
	public void setMessage_to(String message_to) {
		this.message_to = message_to;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	public Long getMessage_timestamp() {
		return message_timestamp;
	}
	public void setMessage_timestamp(Long message_timestamp) {
		this.message_timestamp = message_timestamp;
	}
	public String getMessage_sign() {
		return message_sign;
	}
	public void setMessage_sign(String message_sign) {
		this.message_sign = message_sign;
	}
	
}
