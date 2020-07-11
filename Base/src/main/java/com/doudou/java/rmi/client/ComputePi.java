package com.doudou.java.rmi.client;

import com.doudou.java.rmi.Compute;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ComputePi {
    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/11/20 9:43a
    */
    public static void main(String[] args){
        if (System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry();
            Compute comp = (Compute) registry.lookup(name);
            Pi task = new Pi(Integer.parseInt("1564"));
            BigDecimal pi = comp.executeTask(task);
            System.out.println(pi);

        } catch (RemoteException | NotBoundException e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }
}
