package com.doudou.distribute.rpc.bio.server;

import com.doudou.distribute.rpc.bio.client.UserInterface;
import com.doudou.distribute.rpc.bio.client.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceImpl implements  Server {
    private static final Logger LOGGER = Logger.getLogger(ServiceImpl.class.getName());
    private int port;
    public static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();
    private Executor executor = new ThreadPoolExecutor(10, 200, 10000L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(20));
    public ServiceImpl() {
        this.port = 8888;
    }
    public ServiceImpl(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        ServiceImpl service = new ServiceImpl();
        //注册可以被调用的服务列表
        service.register(UserInterface.class, UserServiceImpl.class);
        service.start();
    }
    @Override
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        try {
            while (true) {
                executor.execute(new ServiceTask(serverSocket.accept()));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "" + e);
        }

    }

    @Override
    public void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    static class ServiceTask implements  Runnable {
        Socket client = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        public ServiceTask(Socket socket) {
            this.client = socket;
        }
        @Override
        public void run() {
            try {
                ois = new ObjectInputStream(this.client.getInputStream());
                String interfaceName = ois.readUTF();
                String methodName = ois.readUTF();
                Object obj;
                Class<?>[] parametertypes = (Class<?>[]) ois.readObject();
                Object[] args = (Object[]) ois.readObject();
                Class serviceClass = serviceRegistry.get(interfaceName);
                if (serviceClass == null) {
                    throw new ClassNotFoundException(interfaceName + " not found !");
                }
                Method method = serviceClass.getMethod(methodName, parametertypes);
                Object result = method.invoke(serviceClass.getDeclaredConstructor().newInstance(), args);
                oos = new ObjectOutputStream(this.client.getOutputStream());
                oos.writeObject(result);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                LOGGER.warning("class not found");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                LOGGER.warning("no such method ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                LOGGER.warning("illegal access");
            } catch (InstantiationException e) {
                e.printStackTrace();
                LOGGER.warning("instantiation exception");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                LOGGER.warning("invocation target exception");
            } finally {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    this.client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
