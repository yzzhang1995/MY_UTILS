import jedis.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jedis的测试类
 */
public class JedisTest {

    private static final String host = "192.168.25.129";
    private static final int port = 6379;

    /**
     * string 数据结构操作
     */
    @Test
    public void test1() {
        //1. 获取连接
        Jedis jedis = new Jedis(host, port);
        //2. 操作
        jedis.set("username", "zhangsan-");
        //获取
        String username = jedis.get("username");
        System.out.println(username);
        //可以使用setex()方法存储可以指定过期时间的 key value
        //将activecode：hehe键值对存入redis，并且20秒后自动删除该键值对
        jedis.setex("activecode", 20, "hehe");

        //3. 关闭连接
        jedis.close();
    }


    /**
     * hash 数据结构操作
     */
    @Test
    public void test2() {
        //1. 获取连接
        Jedis jedis = new Jedis(host, port);//如果使用空参构造，默认值 "localhost",6379端口
        //2. 操作
        // 存储hash
        jedis.hset("user", "name", "lisi");
        jedis.hset("user", "age", "23");
        jedis.hset("user", "gender", "female");

        // 获取hash
        String name = jedis.hget("user", "name");
        System.out.println(name);

        // 获取hash的所有map中的数据
        Map<String, String> user = jedis.hgetAll("user");
        user.forEach((key,value)->System.out.println(key + ":" + value));

        //3. 关闭连接
        jedis.close();
    }


    /**
     * list 数据结构操作
     */
    @Test
    public void test3() {
        //1. 获取连接
        Jedis jedis = new Jedis(host, port);//如果使用空参构造，默认值 "localhost",6379端口
        //2. 操作
        // list 存储
        jedis.lpush("mylist", "a", "b", "c");//从左边存
        jedis.rpush("mylist", "a", "b", "c");//从右边存

        // list 范围获取
        List<String> mylist = jedis.lrange("mylist", 0, -1);
        System.out.println(mylist);

        // list 弹出
        String element1 = jedis.lpop("mylist");//c
        System.out.println(element1);

        String element2 = jedis.rpop("mylist");//c
        System.out.println(element2);

        // list 范围获取
        List<String> mylist2 = jedis.lrange("mylist", 0, -1);
        System.out.println(mylist2);

        //3. 关闭连接
        jedis.close();
    }


    /**
     * set 数据结构操作
     */
    @Test
    public void test4() {
        //1. 获取连接
        Jedis jedis = new Jedis(host, port);//如果使用空参构造，默认值 "localhost",6379端口
        //2. 操作
        // set 存储
        jedis.sadd("myset1", "java","java","php", "c++");

        // set 获取
        Set<String> myset = jedis.smembers("myset1") ;
        System.out.println(myset);

        //3. 关闭连接
        jedis.close();
    }

    /**
     * sortedset 数据结构操作
     */
    @Test
    public void test5() {
        //1. 获取连接
        Jedis jedis = new Jedis(host,port);//如果使用空参构造，默认值 "localhost",6379端口
        //2. 操作
        // sortedset 存储
        jedis.zadd("mysortedset", 3, "亚瑟");
        jedis.zadd("mysortedset", 30, "后裔");
        jedis.zadd("mysortedset", 55, "孙悟空");

        // sortedset 获取
        Set<String> mysortedset = jedis.zrange("mysortedset", 0, -1);

        System.out.println(mysortedset);

        //3. 关闭连接
        jedis.close();
    }

    /**
     * jedis连接池工具类使用
     */
    @Test
    public void test6() {

        //通过连接池工具类获取
        Jedis jedis = JedisPoolUtils.getJedis();

        //3. 使用
        jedis.set("hello", "world");
        //获取
        String hello = jedis.get("hello");
        System.out.println(hello);
        //4. 关闭 归还到连接池中
        jedis.close();
    }
}
