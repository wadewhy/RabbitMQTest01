package xyz.wadewhy.rabbitmq.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Send
 * @Description TODO Work queue
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
    private  static final String QUEUE_NAME = "work_queue rabbitmq";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //获取channel通道
        Channel channel = connection.createChannel();
        //声明队列
        /**
         * 参数1：队列名称
         * 参数2：为true时server重启队列不会消失
         * 参数3：队列是否独占的
         * 参数4：队列不在使用时是否自动删除
         * 参数5：建立队列时的其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //模拟多条消息发送
        for (int i = 0; i <50 ; i++) {
            String message = "hello work_queue"+i;
            System.err.println("----send work----"+message);
            //发送
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            //睡眠
            Thread.sleep(i*5);
        }
        channel.close();
        connection.close();

    }
}
