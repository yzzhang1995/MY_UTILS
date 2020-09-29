package thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.作用：
 * -----CyclicBarrier类可以实现一组线程相互等待，当所有线程都到达某个屏障点后再进行后续的操作。
 * ---对应生活中实际场景 ：
 * -----例如吃饭时要等全家人都上座了才动筷子，
 * -----旅游时要等全部人都到齐了才出发，
 * -----比赛时要等运动员都上场后才开始。
 * 2.原理：
 * ----在CyclicBarrier类的内部有一个计数器，每个线程在到达屏障点的时候
 * ----都会调用await方法将自己阻塞，此时计数器会减1，当计数器减为0的时候
 * ----所有因调用await方法而被阻塞的线程将被唤醒。
 * <p>
 * (1)CyclicBarrier c = new CyclicBarrier(N);
 * ----其参数N表示屏障拦截的线程数量
 * ----public CyclicBarrier(int parties, Runnable barrierAction)
 * ----barrierAction 参数的意思是最后一个线程执行await()方法时，先执行
 * ----barrierAction线程，然后在同时执行之前被阻塞的线程。
 * (2)CyclicBarrier.await()
 * ----线程调用 await() 表示自己已经到达栅栏，然后当前线程被阻塞。
 *
 * @author yzzhang
 * @date 2020/9/1 12:16
 */
public class CyclicBarrierExample {

    /**
     * 让10个runnable线程同时执行，可以看到每个线程打印的时间基本上都是一样的。
     */
    public static void test1() {
        CyclicBarrier c = new CyclicBarrier(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    c.await();//前九个现在到这被阻塞，直到第十个线程到这里然后10个线程同时被唤醒
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Thread:" + Thread.currentThread().getName() + ",time: " + System.currentTimeMillis());

            });
        }
        executorService.shutdown();
    }


    /**
     * 先执行构造参数的先行，然后在执行等待的线程
     */
    public static void test2() {
        CyclicBarrier c = new CyclicBarrier(5, () -> {
            System.out.println("我是第一个执行的");
        });
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                try {
                    c.await();//当第5个线程执行到这时，现在构造函数的线程，在同时执行者五个线程
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Thread:" + Thread.currentThread().getName()
                        + ",time: " + System.currentTimeMillis());
            });
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
//        test1();
        test2();
    }
}
