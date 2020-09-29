package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore（信号量）是用来控制同时访问特定资源的线程数量，它通过协调各个线程，以
 * 保证合理的使用公共资源。
 *
 * @author yzzhang
 * @date 2020/9/1 23:35
 */
public class SemaphoreExample {

    /**
     * 每次只有10个线程执行任务
     */
    public static void test1() {
        ExecutorService threadPool = Executors.newFixedThreadPool(30);
        Semaphore s = new Semaphore(10);
        for (int i = 0; i < 30; i++) {
            threadPool.execute(() -> {
                try {
                    s.acquire();
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(2000);
                    s.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        threadPool.shutdown();
    }

    public static void main(String[] args) {
        test1();
    }
}