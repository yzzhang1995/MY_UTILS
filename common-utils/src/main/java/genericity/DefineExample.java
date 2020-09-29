package genericity;

import java.util.ArrayList;

/**
 * 1.自定义泛型有两种：
 * （1）泛型类：如DefineExample<T>
 * （2）静态方法泛型：如create()
 * 2. 擦拭法
 * ---编译器把类型<T>视为Object；
 * ---编译器根据<T>实现安全的强制转型。
 * 所以：
 * （1）泛型T不能为基本类型
 * （2）无法取得带泛型的Class,即DefineExample<T>的类型永远是DefineExample<Object>，
 * -----故没有T.class方法。
 *
 * @author yzzhang
 * @date 2020/9/29 22:36
 */
public class DefineExample<T> {

    private T first;
    private T last;

    public DefineExample(T first, T last) {
        this.first = first;
        this.last = last;
    }

    public T getFirst() {
        return first;
    }

    public T getLast() {
        return last;
    }

    // 对静态方法使用泛型:
    public static <k> DefineExample<k> create(k first, k last) {
        return new DefineExample<k>(first, last);
    }

    public static void main(String[] args) {
        // 泛型类
        DefineExample<Integer> d1 = new DefineExample<>(1, 2);
        // DefineExample<int> d11 = new DefineExample<>(1, 2);  -- 编译报错
        System.out.println(d1.getFirst() + d1.getLast());

        // 方法泛型
        DefineExample<Double> d2 = DefineExample.create(1.2, 2.4);
        System.out.println(d2.getFirst() + d2.getLast());

        //验证泛型的类型
        System.out.println(d1.getClass() == d2.getClass()) ; // true
        System.out.println(d1.getClass() == DefineExample.class) ;// true



    }
}
