package xyz.wadewhy.rabbitmq.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
 * @Description TODO 消费者
 * @Date 20-4-22 下午10:37
 * @Created by wadewhy
 */
public class Rece1 {
    private final static String QUEUE_NAME = "queue_direct_rabbitmq_1";
    private final static String EXCHANGE_NAME = "exchange_direct_rabbitmq";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, true, null);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.err.println("------Rece1-------" + message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }

        };
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
