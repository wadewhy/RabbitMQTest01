package xyz.wadewhy.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Send
 * @Description TODO topic 通配符模式
 * @Date 20-4-24 上午11:23
 * @Created by wadewhy
 */
public class Send {
    private final static String EXCHANGE_NAME="exchange_topic_rabbitmq";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接和channel
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明交换机,模式为topic
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        //发送消息内容
        String message = "exchange_topic";
        //模拟user的crud
        channel.basicPublish(EXCHANGE_NAME,"user.add",null,message.getBytes());
        //打印
        System.err.println("----send-----:"+message);
        //关闭资源
        channel.close();
        connection.close();
    }
}
