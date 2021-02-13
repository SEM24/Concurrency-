package com.company;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private final int MAX = 50_000;
    public int count = 0;
    AtomicInteger value = new AtomicInteger(0);
    //чтобы рандомизировать число и был больше шанс показать ошибку
    public int expected = (int) (Math.random() * MAX + 2000);

    public static void main(String[] args) {
        new Main().run();
        //  new Main().run2();
//        new Main().run3();
    }

    private void run() {
        /**
         * Создать expected одновременных задач, которые увеличивают целочисленный счетчик на 1.
         * Подтвердить проблему атомарности. Проверить ее решение с помощью Atomic.
         * Выполнить ожидание завершения задач с помощью CountDownLatch.
         */
        ExecutorService executorService = Executors.newFixedThreadPool(200);

        CountDownLatch doneSignal = new CountDownLatch(expected);

        final Runnable task = () -> {
            count++;
            value.getAndIncrement();
            doneSignal.countDown();
        };
        System.out.println("Должно быть: " + expected);

        for (int i = 0; i < expected; i++) {
            executorService.submit(task);
        }
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Текущее атомик " + value);
        System.out.println("Текущее обычный вариант " + count);
        executorService.shutdownNow();
    }


}
