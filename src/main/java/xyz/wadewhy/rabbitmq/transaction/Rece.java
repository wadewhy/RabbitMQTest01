package xyz.wadewhy.rabbitmq.transaction;

import com.rabbitmq.client.*;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Rece
 * @Description TODO 事务消息确认模式消费癌症
 * @Date 20-4-24 下午2:19
 * @Created by wadewhy
 */
public class Rece {
    private final static  String QUEUE_NAME="queue_name_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接和channel
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明列名
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //消费消息
        channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.err.println("rece------------>:"+message);
            }
        });
    }
}
