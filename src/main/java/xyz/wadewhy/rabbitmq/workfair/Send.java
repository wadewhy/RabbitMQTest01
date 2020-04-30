package xyz.wadewhy.rabbitmq.workfair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Send
 * @Description TODO Work queue之公平分发,手动反馈消费完毕信息
 * @Date 20-4-21 下午11:07
 * @Created by wadewhy
 */
/*
                         |------C1
     p--------Queue------|
                         |------C2
 */
public class Send {
    //queue_name
    private  static final String QUEUE_NAME = "workfair_queue rabbitmq";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //获取channel通道
        Channel channel = connection.createChannel();
        /**
         * 在没有得到消费者的反馈之前，消息队列不发送消息给该消费者
         */
        //每次发一个
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        //模拟多条消息发送
        for (int i = 0; i <50 ; i++) {
            String message = "hello work_queue"+i;
            System.err.println("----send work----"+message);
            //发送
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            //睡眠
            Thread.sleep(1*5);
        }
        channel.close();
        connection.close();

    }
}
