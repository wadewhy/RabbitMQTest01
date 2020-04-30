package xyz.wadewhy.rabbitmq.pubsub;

import com.rabbitmq.client.*;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Rece2
 * @Description TODO 消费者
 * @Date 20-4-22 下午9:42
 * @Created by wadewhy
 */
public class Rece2 {
    /**
     * 1.队列名称和交换机名称
     * 2.获取连接和channel通道
     *  声明队列
     * 3.绑定队列到交换机
     * 4.满足公平分发[能者多劳]
     * 5.触发事件
     * 6.获取消息
     * 7.反馈消费状态
     */
    //1.队列名称和交换机名称[队列名模拟发邮件]
    private final static String QUEUE_NAME="queue_exchange_EMAIL";
    private final static String EXCHANGE_NAME="exchange_fanout_rabbitmq";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 2.获取连接和channel通道
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //3.绑定队列到交换机
        /**
         * 交换机名字要和Send里的一致
         * 参数1：队列名称
         * 参数2：交换机名称
         * 参数3：Routing keys
         */
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
        //满足公平分发,每次发一个，得到消费完成反馈后发下一个
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.err.println("------Rece2接收------：" + message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("【Rece2】 done");
                    //反馈消费状态
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        //关闭自动应答
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
