package im.protoc;

import java.io.Serializable;

/**
 * Created by Misnearzhang on 2017/3/1.
 */
public class Header implements Serializable{
    private String type;// 1,普通用户间消息  2.响应消息  3.系统通知消息 4.心跳 5.用户验证请求 6.用户验证结果
    private String status;// {请求消息 1. 100:请求信息}    {响应消息  2.200:响应正常(登录验证的响应为有数据响应)     3.404:响应无数据(登录验证时响应无数据)    4.500:响应服务器报错}
    private String uid;//消息编码  响应的消息该字段为原消息编码

    public String getType() {
        return type;
    }

    public Header setType(String type) {
        this.type = type;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Header setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public Header setUid(String uid) {
        this.uid = uid;
        return this;
    }
}
