package com.company;

import java.util.concurrent.atomic.AtomicInteger;

public class SingletonSolution {

    private static SingletonSolution instance;
    private static AtomicInteger atomicInteger;

    private SingletonSolution() {
        atomicInteger = new AtomicInteger(0);
    }

    public static SingletonSolution getInstance() {
        SingletonSolution localInstance = instance;
        if (localInstance == null) {
            synchronized (SingletonSolution.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SingletonSolution();
                }
            }
        }
        System.out.println("Atomic счетчик " + atomicInteger.incrementAndGet());
        return localInstance;
    }
}
