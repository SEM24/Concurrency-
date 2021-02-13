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

    private void run2() {
        /**
         * Получить доступ к singleton-объекту с “ленивой” (lazy) инициализацией
         * из множества потоков с использованием барьера инициализации при помощи класса CountDownLatch.
         * Подтвердить проблему атомарности. Решить ее.
         */

        CountDownLatch doneSignal = new CountDownLatch(expected);
        ExecutorService executor = Executors.newFixedThreadPool(expected);

        final Runnable task = () -> {
            Singleton.getInstance();
            SingletonSolution.getInstance();
            doneSignal.countDown();
        };
        for (int i = 0; i < expected; i++) {
            executor.submit(task);
        }

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    private void run3() {
        /**
         * Create a simple java program with 2 methods.
         * One method prints “Hello” and the other method prints “World”.
         * Call these methods concurrently & asynchronously. That is, both methods are invoked at the same instant, not one after the other.
         * So on console you should sometimes see “World Hello” and sometimes “Hello World” depending on which method is invoked first.
         * Repeat these calls every 10 seconds and stop after 1 minute.
         * Expected Result: On the console you should see 6 combinations, a mix of “Hello World” and “World Hello”.
         */

        HelloWorld helloWorld = new HelloWorld();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        Runnable run = () -> {
            scheduler.execute(() -> {
                try {
                    countDownLatch.await();
                    helloWorld.showHello();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            scheduler.execute(() -> {
                try {
                    countDownLatch.await();
                    helloWorld.showWorld();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            countDownLatch.countDown();
        };

        ScheduledFuture<?> helloWorldResult =
                scheduler.scheduleAtFixedRate(run, 0, 10, TimeUnit.SECONDS);
        scheduler.schedule(() -> {
            helloWorldResult.cancel(true);
            scheduler.shutdown();
        }, 60, TimeUnit.SECONDS);
    }

}
