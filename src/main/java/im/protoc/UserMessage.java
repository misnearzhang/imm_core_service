package im.protoc;

import java.io.Serializable;

/**
 * 消息发起抽象 可用其他序列化工具代替
 * @author Misnearzhang
 *
 */
public class UserMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6808713478447609004L;
	private String uid;//UUID  128bit 
	private String from;// system friend group 
	private String to;//	system friend broadcast
	private String type;//sys user 
	private String content;//加密字符串
	private Long timestamp;
	private String sign;//校验和
	
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "Message [uid=" + uid + ", from=" + from + ", to=" + to + ", type=" + type + ", content=" + content
				+ ", timestamp=" + timestamp + ", sign=" + sign + "]";
	}
}
