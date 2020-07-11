package com.doudou.java.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ComputeEngine implements Compute {

    public ComputeEngine(){
        super();
    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        return t.execute();
    }

    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/11/19 18:23a
    */
    public static void main(String[] args){
        if (System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        try {
           // LocateRegistry.createRegistry(1099);
            String name = "Compute";
            Compute engine = new ComputeEngine();
            Compute stub =
                (Compute) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (RemoteException e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}
