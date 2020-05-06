package xyz.wadewhy.rabbitmq.workfair;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Rece1
 * @Description TODO
 * @Date 20-4-21 下午11:16
 * @Created by wadewhysd
 */
public class Rece2 {
    // queue_name
    private static final String QUEUE_NAME = "workfair_queue rabbitmq";

    // 计算消费次数
    private static int count = 0;

    public static void main(String[] args) throws Exception {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        final Channel channel = connection.createChannel();

        channel.basicQos(1);// 每一次分发一个

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 定义消费者事件，事件触发
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.err.println("------Rece2接收------：" + message);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("【Rece2】 done消费者2消费次数：" + count);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        // 关闭自动应答
        boolean autoAsk = false;

        channel.basicConsume(QUEUE_NAME, autoAsk, consumer);

    }
}
