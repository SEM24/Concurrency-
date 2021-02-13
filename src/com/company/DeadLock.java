package com.company;

public class DeadLock implements Runnable {
    /**
     * Воспроизвести проблему dead lock
     */
    private static class Information {
    }

    private final Information lockOne = new Information();
    private final Information lockTwo = new Information();

    public void doFirst() {
        synchronized (lockOne) {
            System.out.println(Thread.currentThread().getName()
                    + " Подготовка к действию #1");
            synchronized (lockTwo) {
                System.out.println(Thread.currentThread().getName()
                        + " Подготовка к действию #2");
                System.out.println(Thread.currentThread().getName()
                        + " Выполнение_1");
            }
        }
    }

    public void doSecond() {
        synchronized (lockTwo) {
            System.out.println(Thread.currentThread().getName()
                    + " Подготовка к действию #3");
            synchronized (lockOne) {
                System.out.println(Thread.currentThread().getName()
                        + " Подготовка к действию #4");
                System.out.println(Thread.currentThread().getName()
                        + " Выполнение_2");
            }
        }
    }

    @Override
    public void run() {
            doFirst();
            doSecond();
    }

    public static void main(String[] args) {
        DeadLock make = new DeadLock();
        Thread first = new Thread(make,"Первый");
        Thread second = new Thread(make,"Второй");
        first.start();
        second.start();
    }
}
