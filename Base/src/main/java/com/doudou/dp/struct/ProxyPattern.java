package com.doudou.dp.struct;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 代理模式
 */
public class ProxyPattern {

    public static void main(String[] args) {
        //普通代理模式
        IGamePlayer proxy = GamePlayerProxy.create("张三");
        proxy.login("ZhangSan", "123456");
        proxy.killBoss();
        proxy.upgrade();

        //强制代理模式
        IGamePlayer gamePlayer = new GamePlayer("李四");
        IGamePlayer proxyPlayer = gamePlayer.getProxy();
        proxyPlayer.login("LiSi", "123456");
        proxyPlayer.killBoss();
        proxyPlayer.upgrade();


        //动态代理，JDK的动态代理
        IGamePlayer gamePlayer1 = new GamePlayer("豆豆");
        InvocationHandler handler = new GamePlayIH(gamePlayer1);
        ClassLoader loader = gamePlayer1.getClass().getClassLoader();
        //动态生成代理
        IGamePlayer proxy1 = (IGamePlayer) java.lang.reflect.Proxy
                .newProxyInstance(loader, new Class[]{IGamePlayer.class}, handler);
        proxy1.login("dpw", "123456");
        proxy1.killBoss();
        proxy1.upgrade();


        //基于Jdk动态代理的AOP
        IGamePlayer gamePlayer2 = new GamePlayer("DD");
        InvocationHandler handler1 = new GamePlayIH(gamePlayer2);
        IGamePlayer dynamicProxy = DynamicProxy.newProxyInstance(gamePlayer2.getClass().getClassLoader(),
                gamePlayer2.getClass().getInterfaces(), handler1);
        dynamicProxy.login("dd", "123654");
        dynamicProxy.killBoss();
        dynamicProxy.upgrade();

    }

}

//========================普通代理==============================
//客户端只能访问代理角色，而不能访问真实角色。
//代码层面上就是控制真实角色的构造权限，无法直接实例化出真实角色

//==========================强制代理============================
//必须通过真实的角色找到代理

//**********************代理通用的能力********************
//代理类也可以有自己的独有的特性，比方说代打游戏需要收费
//可以增加一个代理的接口，声明特有的能力


//一个代理也可以代理多个角色，多个角色之间可以耦合。
//*******************************************************

//=====================动态代理==========================
//实现阶段不用关心代理谁，而在运行阶段才指定代理哪一个对象。
//AOP的核心就是动态代理机制


interface IGamePlayer {
    void login(String user, String pwd);
    void killBoss();
    void upgrade();

    /**
     * 强制代理新增找代理的方法
     * @return IGamePlayer
     */
    IGamePlayer getProxy();
}

/**
 * 代理接口，增加代理独有特性
 */
interface Proxy {
    void count();
}


/**
 * 对被代理类的方法进行代理
 * @see java.lang.reflect.InvocationHandler jdk提供的动态代理接口
 */
class GamePlayIH implements InvocationHandler {
    //被代理者
    Class clazz = null;

    //被代理的实例
    Object object = null;

    //我要代理谁
    public GamePlayIH(Object object) {
        this.object = object;
    }

    //调用被代理的方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(this.object, args);
        if (method.getName().equals("login")) {
            System.out.println("有人在登录账号：" + args[0]);
        }
        new AfterAdvice().exec();
        return result;
    }
}

class GamePlayer implements IGamePlayer {
    private String name = "";

    private GamePlayerProxy playerProxy;

    //构造函数控制访问权限
    protected GamePlayer(IGamePlayer gamePlayer, String name) {
        if (Objects.isNull(gamePlayer)) {
            System.out.println("不能创建真实对象");
        }

        this.name = name;
    }

    /**
     * 强制代理新增的构造函数
     * @param name 名字
     */
    public GamePlayer(String name) {
        this.name = name;
    }

    @Override
    public void login(String user, String pwd) {
        if (isProxy())
            System.out.println("当前登录的用户是：" + name);
        else
            System.out.println("必须通过代理类来访问");
    }

    @Override
    public void killBoss() {
        if (isProxy())
            System.out.println(name + " killed boss!!");
        else
            System.out.println("必须通过代理类来访问");
    }

    @Override
    public void upgrade() {
        if (isProxy())
            System.out.println(name + " 升级了！！！");
        else
            System.out.println("必须通过代理类来访问");
    }

    @Override
    public IGamePlayer getProxy() {
        this.playerProxy = new GamePlayerProxy(this);
        return this.playerProxy;
    }

    /**
     * 判断是否是代理类在调用
     * @return boolean
     */
    private boolean isProxy() {
       return this.playerProxy != null;
    }
}


class GamePlayerProxy implements IGamePlayer, Proxy{
    private IGamePlayer iGamePlayer;
    private int count = 0;

    private GamePlayerProxy() {
    }

    public GamePlayerProxy(IGamePlayer gamePlayer) {
        this.iGamePlayer = gamePlayer;
    }

    public static IGamePlayer create(String name) {
        GamePlayerProxy playerProxy = new GamePlayerProxy();
        playerProxy.iGamePlayer = new GamePlayer(playerProxy, name);
        return playerProxy;
    }

    @Override
    public void login(String user, String pwd) {
        this.iGamePlayer.login(user, pwd);
    }

    @Override
    public void killBoss() {
        this.iGamePlayer.killBoss();
    }

    @Override
    public void upgrade() {
        this.iGamePlayer.upgrade();
        this.count();
    }

    @Override
    public IGamePlayer getProxy() {
        return this;
    }

    @Override
    public void count() {
        count += 5;
        System.out.println("完成一次升级，当前收费：" + count + "元");
    }
}

//=======================AOP的JDK动态代理实现===========================

//首先来定义一个通知
interface Advice {
    void exec();
}


//定义一个前置通知
class BeforeAdvice implements Advice {
    @Override
    public void exec() {
        System.out.println("执行前置通知！！！！");
    }
}

//定义一个后置通知
class AfterAdvice implements Advice {
    @Override
    public void exec() {
        System.out.println("执行后置通知！！！！");
    }
}

//定义一个动态代理类
class DynamicProxy {
    public static <T>  T newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler handler) {
        //寻找JoinPoint连接点，AOP框架使用元数据定义
        if (true) {
            //执行前置通知
            new BeforeAdvice().exec();
        }
        T proxyInstance = (T) java.lang.reflect.Proxy.newProxyInstance(loader, interfaces, handler);
        return proxyInstance;
    }
}


//看一下Spring的AOP

class SpringAOP {
    org.springframework.aop.BeforeAdvice beforeAdvice;
    org.springframework.aop.AfterAdvice afterAdvice;
    AfterReturningAdvice afterReturningAdvice;

}