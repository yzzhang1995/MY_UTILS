package thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exchanger（交换者）是一个用于线程间协作的工具类。
 * Exchanger用于进行线程间的数据交换。
 * 它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据。
 * 这两个线程通过exchange方法交换数据，如果第一个线程先执行exchange()方法，
 * 它会一直等待第二个线程也执行exchange方法，当两个线程都到达同步时，
 * 这两个线程就可以交换数据，将本线程生产出来的数据传递给对方。
 *
 * @author yzzhang
 * @date 2020/9/1 23:35
 */
public class ExchangerExample {

    /**
     * 交换2个线程的数据
     */
    public static void test1() {
        Exchanger<String> exgr = new Exchanger<String>();
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                try {
                    String A = "String" + finalI;
                    String exchange = exgr.exchange(A);
                    System.out.println(Thread.currentThread().getName() + " " + exchange);
                } catch (InterruptedException e) {
                }
            });
        }
        threadPool.shutdown();

    }

    public static void main(String[] args) {
        test1();
    }
}
