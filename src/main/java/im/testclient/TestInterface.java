package im.testclient;

public interface TestInterface {

    void test();
    default void test2(){
        System.out.println("default 方法");
    }
}
