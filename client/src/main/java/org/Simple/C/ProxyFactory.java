package org.Simple.C;


import org.Simple.API.NetModel;
import org.Simple.API.SerializeUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ProxyFactory {

    private static InvocationHandler handler = new InvocationHandler() {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            NetModel netModel = new NetModel();
            Class<?> c = proxy.getClass();
            System.out.println(c.getName());
            Class<?>[] classes = c.getInterfaces();
            String className = classes[0].getName();

            for (int i = 0; i < args.length; i++) {
               Object aClass = args[i];
                System.out.println("参数" + aClass);
            }

            netModel.setClassName(className);
            System.out.println(className);
            netModel.setArgs(args);
            netModel.setMethod(method.getName());
            String [] types = null;
            if(args!=null) {
                types = new String [args.length];
                for (int i = 0; i < types.length; i++) {
                    types[i] = args[i].getClass().getName();
                }
            }
            netModel.setTypes(types);

            byte[] byteArray = SerializeUtils.serialize(netModel); // 被调用的接口以及方法的详细内容
            Object send = send(byteArray);
            return send;

        }
    };

    //Socket发送消息给服务端,并反序列化服务端返回的数据,返回给方法调用者
    public static Object send(byte[] data)  {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(data);
            InputStream inputStream = socket.getInputStream();
            byte[] buf = new byte[1024];
            inputStream.read(buf);
            Object formatData = SerializeUtils.deSerialize(buf);
            System.out.println("返回数据" + formatData);
            socket.close();
            return formatData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, handler);
    }
}
