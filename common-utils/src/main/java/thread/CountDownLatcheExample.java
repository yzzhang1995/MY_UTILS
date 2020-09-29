package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * CountDownLatch的用法：
 * CountDownLatch允许一个或多个线程等待其他线程完成操作。
 * （1）CountDownLatch countDownLatch = new CountDownLatch(1);
 * -----CountDownLatch的构造函数接收一个int类型的参数作为计数器，如果你想等待N个点完成，这里就传入N。
 * （2）countDownLatch.await()
 * -----CountDownLatch的await方法会阻塞当前线程，直到N变成零。此外还提供了一个超时等待的方法。
 * （3）countDownLatch.countDown();
 * -----调用一次countDown()则计数器减一
 *
 * @author yzzhang
 * @date 2020/8/31 12:36
 */
public class CountDownLatcheExample {

    /**
     * 让10个runnable线程同时执行，可以看到每个线程打印的时间基本上都是一样的。
     */
    public static void testRunnable() {
        //这里的1表示 main线程的 countDownLatch.countDown();执行完后同时执行10个线程。
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    countDownLatch.await();
                    System.out.println("Thread:" + Thread.currentThread().getName()
                            + ",time: " + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
        countDownLatch.countDown();
        executorService.shutdown();
    }

    /**
     * 让10个callable线程同时执行，可以看到每个线程打印的时间基本上都是一样的。
     */
    public static void testCallable() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Callable<String> task;
        List<Callable<String>> tasks = new ArrayList<>();
        System.out.println("0");
        for (int i = 0; i < 10; i++) {
            task = () -> {
                // System.out.println();
                return "Thread:" + Thread.currentThread().getName() + ",time: " + System.currentTimeMillis();
            };
            tasks.add(task);
        }
        countDownLatch.countDown();
        List<Future<String>> futures = executorService.invokeAll(tasks);
        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    /**
     * 等待10个runnable线程执行完毕，主线程在执行。去掉CountDownLatch则主线程和各个线程是交叉执行的。
     */
    public static void testRunnable2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println("Thread:" + Thread.currentThread().getName() + ",time: " + System.currentTimeMillis());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("主线程执行....");
        executorService.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        testRunnable2();
    }
}
