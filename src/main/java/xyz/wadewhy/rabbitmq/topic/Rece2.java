package xyz.wadewhy.rabbitmq.topic;

import com.rabbitmq.client.*;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Rece1
 * @Description TODO topic 模式下的消费者
 * @Date 20-4-24 上午11:28
 * @Created by wadewhy
 */
public class Rece2 {
    private final static String EXCHANGE_NAME="exchange_topic_rabbitmq";
    private final static String QUEUE_NAME="topic_queue_name_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接和channel
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        //声明交换机,模式为topic[消费断声明交换机是为了避免先启动消费者，没有交换机会出异常]
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定队列和交换机 "#"匹配所有
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"user.#");
        //确保同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        boolean autoAsk = false;//关闭自动应答
        //监听队列，手动返回回应
        channel.basicConsume(QUEUE_NAME,autoAsk,new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.err.println("rece[2]----------"+message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("【Rece2】 done");
                    //手动回应,反馈消费状态
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }
}
