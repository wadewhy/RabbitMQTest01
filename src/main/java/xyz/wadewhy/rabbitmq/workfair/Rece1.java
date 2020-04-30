package xyz.wadewhy.rabbitmq.workfair;

import com.rabbitmq.client.*;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;

/**
 * 作者博客：wadewhy.xyz
 * @Classname Rece1
 * @Description TODO
 * @Date 20-4-21 下午11:16
 * @Created by wadewhy
 */
public class Rece1 {
    //queue_name
    private static final String QUEUE_NAME = "workfair_queue rabbitmq";


    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //获取通道
        final Channel channel = connection.createChannel();
        channel.basicQos(1);//每一次分发一个
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //定义消费者事件，事件触发
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            //触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.err.println("------Rece1接收------：" + message);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("【Rece1】 done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAsk = false;//关闭自动应答
        channel.basicConsume(QUEUE_NAME, autoAsk, consumer);

    }
}
