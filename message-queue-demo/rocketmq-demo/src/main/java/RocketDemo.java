import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author yzzhang
 * @date 2020/9/12 11:29
 */
public class RocketDemo {
    public static final String IP = "192.168.25.130:9876";
    public static DefaultMQProducer producer;
    public static DefaultMQPushConsumer consumer;
    public static TransactionMQProducer transactionProducer;

    /**
     * 初始化事务型生产者Producer
     */
    public static void initTransactionProducer() {
        transactionProducer = new TransactionMQProducer("transaction_producer");
        transactionProducer.setNamesrvAddr(IP);
    }

    /**
     * 初始化生产者Producer
     */
    public static void initProducer() {
        producer = new DefaultMQProducer("HAOKE_IM");
        producer.setNamesrvAddr(IP);
    }

    /**
     * 初始化消费者Producer
     */
    public static void initConsumer() {
        consumer = new DefaultMQPushConsumer("HAOKE_IM");
        consumer.setNamesrvAddr(IP);
    }

    /**
     * 创建topic
     *
     * @throws MQClientException
     */
    public static void createTopic() throws MQClientException {

        producer.start();
        /*
         * key：broker名称,必须先在 broker.conf中配置对应的信息
         * newTopic：topic名称
         * queueNum：队列数（分区）
         */
        producer.createTopic("broker_haoke_im", "haoke_im_topic2", 8);
        System.out.println("创建topic成功");
        producer.shutdown();
    }


    /**
     * 同步发送消息
     */
    public static void syncProducer() throws Exception {
        producer.start();
        String msgStr = "用户A发送消息给用户B";
        Message msg = new Message("haoke_im_topic", "SEND_MSG",
                msgStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 发送消息
        SendResult sendResult = producer.send(msg);
        System.out.println("消息状态：" + sendResult.getSendStatus());
        System.out.println("消息id：" + sendResult.getMsgId());
        System.out.println("消息queue：" + sendResult.getMessageQueue());
        System.out.println("消息offset：" + sendResult.getQueueOffset());
        producer.shutdown();
    }

    /**
     * 异步发送消息
     */
    public static void AsyncProducer() throws Exception {
        producer.start();
        String msgStr = "用户A发送消息给用户B-异步";
        Message msg = new Message("haoke_im_topic", "SEND_MSG",
                msgStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 异步发送消息
        producer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("消息状态：" + sendResult.getSendStatus());
                System.out.println("消息id：" + sendResult.getMsgId());
                System.out.println("消息queue：" + sendResult.getMessageQueue());
                System.out.println("消息offset：" + sendResult.getQueueOffset());
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送失败！");
                e.printStackTrace();
            }
        });
        System.out.println("发送成功!");
        //  producer.shutdown()要注释掉，否则发送失败。原因是，异步发送，还未来得及发送就被关闭了。
        // producer.shutdown();
    }

    /**
     * 消费消息
     */
    public static void ConsumerMessage() throws Exception {

        // 订阅topic，接收此Topic下的所有消息
        consumer.subscribe("haoke_im_topic", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    System.out.println(new String(msg.getBody(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("收到消息->" + msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }

    /**
     * 消息过滤器 - 生产者,ConsumerFilter是对应的消费者
     */
    public static void SyncProducerFilter() throws Exception {
        producer.start();
        String msgStr = "美女002";
        Message msg = new Message("haoke_meinv_topic", "SEND_MSG",
                msgStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
        msg.putUserProperty("age", "30");
        msg.putUserProperty("sex", "女");
        // 发送消息
        SendResult sendResult = producer.send(msg);
        System.out.println("消息状态：" + sendResult.getSendStatus());
        System.out.println("消息id：" + sendResult.getMsgId());
        System.out.println("消息queue：" + sendResult.getMessageQueue());
        System.out.println("消息offset：" + sendResult.getQueueOffset());
        System.out.println(sendResult);
        producer.shutdown();
    }

    /**
     * 消息过滤器 - 消费者
     */
    public static void ConsumerFilter() throws Exception {
        // 订阅topic，接收此Topic下的所有消息
        consumer.subscribe("haoke_meinv_topic"
                , MessageSelector.bySql("sex='女'"));
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    System.out.println(new String(msg.getBody(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("收到消息->" + msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("消费结束");
    }


    /**
     * 顺序- 生产者 对应的消费者为OrderConsumer
     */
    public static void OrderProducer() throws Exception {
        producer.start();
        for (int i = 0; i < 100; i++) {
            int orderId = i % 10; // 模拟生成订单id
            String msgStr = "order --> " + i + " id=" + orderId;
            Message message = new Message("haoke_order_topic", "ORDER_MSG",
                    msgStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
            /*
               这里的arg对应orderId，mqs为List<MessageQueue> ，即 一个topic中message的数量，这里默认是4。
               程序的逻辑是根据mqs的个数把orderId分到对应 的mqsID中
             */
            SendResult sendResult = producer.send(message, (mqs, msg, arg) -> {
                Integer id = (Integer) arg;
                int index = id % mqs.size();
                return mqs.get(index);
            }, orderId);
            System.out.println(sendResult);
        }
        producer.shutdown();
    }

    /**
     * 顺序- 消费者
     * 可以看到相同ID的消息被分配到同一个queueID和线程中。
     */
    public static void OrderConsumer() throws Exception {
        consumer.subscribe("haoke_order_topic", "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
                                                       ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " "
                                + msg.getQueueId() + " "
                                + new String(msg.getBody(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
    }

    /**
     * 分布式事务-生产者
     */
    public static void transactionProducer() throws Exception {
        // 设置事务监听器
        transactionProducer.setTransactionListener(new TransactionListenerImpl());
        transactionProducer.start();
        // 发送消息
        Message message = new Message("pay_topic", "用户A给用户B转账500 元".getBytes("UTF-8"));
        transactionProducer.sendMessageInTransaction(message, null);
        Thread.sleep(999999);
        transactionProducer.shutdown();
    }

    /**
     * 分布式事务-消费者
     */
    public static void transactionConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new
                DefaultMQPushConsumer("HAOKE_CONSUMER");
        consumer.setNamesrvAddr(IP);
        // 订阅topic，接收此Topic下的所有消息
        consumer.subscribe("pay_topic", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    System.out.println(new String(msg.getBody(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }

    public static void main(String[] args) throws Exception {
        initProducer();
        initTransactionProducer();
        initConsumer();
//        createTopic();
//        syncProducer();
//        AsyncProducer();
//       ConsumerMessage();
//        SyncProducerFilter();
//        ConsumerFilter();
//       OrderProducer();
//        OrderConsumer();//
//        transactionProducer();
        transactionConsumer();
    }
}
