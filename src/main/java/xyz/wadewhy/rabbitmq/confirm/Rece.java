package xyz.wadewhy.rabbitmq.confirm;

import com.rabbitmq.client.*;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Rece
 * @Description TODO
 * @Date 20-4-24 下午2:59
 * @Created by wadewhy
 */
public class Rece {
    private final  static String QUEUE_NAME = "queue_name_confirm";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接和channel
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //创建消费队列
        channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String (body,"utf-8");
                System.err.println("------rece--------:"+message);
                System.out.println(" ConfirmReceiver1 Done! at " + time.format(new Date()));
            }
        });

    }
}
