package im.testclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;


import static java.lang.Long.BYTES;
import static java.lang.Long.reverseBytes;

/**
 * Created by misnearzhang on 2017/5/10.
 */
public class Test {
    public static void main(String[] args) {
        /*long start = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            String s= UUID.randomUUID().toString();
            System.out.println(s);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);*/
        int m=4323;
        System.out.println(m&0xffffffff);
        System.out.println((byte)(m>>24)&0xff);
        System.out.println((byte)(m>>16)&0xff);
        System.out.println((byte)(m>>8)&0xff);
        System.out.println((byte)(m>>0)&0xff);
        ArrayList<Message> list = new ArrayList<>(100);
        for (int i = 0; i <100; i++) {
            Message message = new Message();
            message.setAge(10);
            message.setUuid(UUID.randomUUID().toString());
            message.setUserName("");
            message.setPassword("");
            message.setSex("");
            list.add(message);
        }


    }


    public static class Message{
        private String uuid;
        private String userName;
        private String password;
        private String sex;
        private Integer age;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

}
