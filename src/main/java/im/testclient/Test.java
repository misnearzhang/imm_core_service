package im.testclient;

import im.process.ParseTask;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        /*int m=4323;
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
        }*/
        /*String inedxUID= "";
        ArrayList<Message> userList = new ArrayList<Message>(1000000);
        for (int i = 0; i < 1000000; i++) {
            Message message = new Message(i);
            userList.add(message);
            if(i==50000){
                inedxUID = message.getUuid();
            }
        }
        Long start = System.currentTimeMillis();
        String finalInedxUID = inedxUID;
        List<Message> collect = userList.stream().filter(p->p.getUuid().equals(finalInedxUID)).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(System.currentTimeMillis()-start);*/

        String Parse_String  = ParseTask.class.getName();
        System.out.println(Parse_String);
    }


    public static class Message{
        private String uuid;
        private String userName;
        private String password;
        private String sex;
        private Integer age;

        public Message(int index) {
            this.uuid = UUID.randomUUID().toString();
            this.userName = "zhanglong"+index;
            this.password = "";
            this.sex = "";
            this.age = index;
        }

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

        @Override
        public String toString() {
            return "Message{" +
                    "uuid='" + uuid + '\'' +
                    ", userName='" + userName + '\'' +
                    ", password='" + password + '\'' +
                    ", sex='" + sex + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
