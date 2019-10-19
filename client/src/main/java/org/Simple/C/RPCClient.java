package org.Simple.C;

import org.Simple.API.HelloService;

public class RPCClient {

    //main方法,运行客户端
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getInstance(HelloService.class);
        System.out.println(helloService.getClass().getName());
        System.out.println("say:"+helloService.sayHello("zhangsan"));
        System.out.println("Person:"+helloService.getPerson("zhangsan"));

    }

}
