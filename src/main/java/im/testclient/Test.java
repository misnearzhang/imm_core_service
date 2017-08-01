package im.testclient;

import java.util.*;

/**
 * Created by misnearzhang on 2017/5/10.
 */
public class Test {
    private static volatile boolean isShutDown=true;
    public static void main(String[] args) {
        Integer[] index = {10,555,888,10000,66661,23,999,2,6423,9977,211,9990};
        ArrayList<String> A = new ArrayList<>(10000000);

        for (int i = 0; i < 10000000; i++) {
            A.add("hello"+i);
        }

        long start = System.currentTimeMillis();

        for (int i = 0; i < index.length; i++) {
            A.remove(index[i]);
        }
        System.out.println(System.currentTimeMillis()-start);

        /*Arrays.sort(index);
        System.out.println(Arrays.toString(index));
        long start = System.currentTimeMillis();
        ArrayList<String> B = new ArrayList<>(10000000-index.length);
        int pos=0;
        for(int i=0;i<index.length;i++){
            int length=0;
            if(i==0){
                length=index[0];
            }else{
                length=index[i]-index[i-1];
            }
            if(i==0){
                B.addAll(0,A.subList(0,index[i]));
                //System.arraycopy(A,0,B,0,length);
            }else{
                //System.arraycopy(A,index[i],B,pos,length);
                B.addAll(pos,A.subList(index[i-1]+1,index[i]));
            }
            pos+=length-1;
            if(i==index.length-1){
                B.addAll(pos-1,A.subList(index[i]+1,A.size()));
            }
        }
        System.out.println(System.currentTimeMillis()-start);*/
    }
}
