package xyz.wadewhy.rabbitmq.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

/**
 * 作者博客：wadewhy.xyz
 * 
 * @Classname Send
 * @Description TODO 路由模式
 * @Date 20-4-22 下午10:17
 * @Created by wadewhy
 */
public class Send {
    private final static String EXCHANGE_NAME = "exchange_direct_rabbitmq";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明exchange
        /**
         * 参数1：交换机名称 参数2：交换机类型 参数3：交换机吃就行，如果为true则服务器重启时不会丢失 参数4：交换机再不被使用时是否删除
         * 参数5：交换机的其他属性
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, true, null);
        // 模拟消息内容
        String message = "exchange_direct_rabbitmq-send----->info";
        /**
         * 向server发布一条消息 参数1：exchange名字，若为空则使用默认的exchange 参数2：direct key 参数3：其他的属性
         * 参数4：消息体 RabbitMQ默认有一个exchange，叫default exchange，它用一个空字符串表示，它是direct
         * exchange类型， 任何发往这个exchange的消息都会被路由到routing key的名字对应的队列上，如果没有对应的队列，则消息会被丢弃
         */
        channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes());
        System.err.println("-----direct-key-send------" + message);
        channel.close();
        connection.close();

    }

}
