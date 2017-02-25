package im.protoc;

import java.io.Serializable;

/**
 * 通用消息响应
 * @author Misnearzhang
 *
 */
public class Response implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8263986380809806351L;
	
	private String uid;//UUID 128bit
	private String msgId;
	private int status; //200:success  500 : system error 403:sign error  etc.
	private String des;// describe
	private Long timestamp;
	
	
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "Response [uid=" + uid + ", msgId=" + msgId + ", status=" + status + ", timestamp=" + timestamp + "]";
	}
	
	
}
