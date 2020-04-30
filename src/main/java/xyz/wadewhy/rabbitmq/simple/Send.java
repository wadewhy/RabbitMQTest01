package xyz.wadewhy.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Send
 * @Description  TODO 发送者
 * @Date 20-4-21 下午10:12
 * @Created by wadewhy
 */
public class Send {
    //队列名
    private final static String QUEUE_NAME="simple_rabbitmq";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connectiont = ConnectionUtil.getConnection();
        //获取channel通道，
        Channel channel = connectiont.createChannel();
        /**
         *  声明(创建)队列
         *  参数1：队列名称
         *  参数2：为true时server重启队列不会消失
         *  参数3：队列是否时独占胡，如果为true只能被一个connection使用，其他的connection连接会报错
         *  参数4：队列不再使用时是否自动删除(没有连接，并且没有未处理的消息)
         *  参数5：建立队列时的其他参数
         *
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发送的队列消息
        String message = "hello simple rabbitmq";
        /**
         * 向server发布一条消息
         * 参数1：exchange名字，为空时使用默认的exchange
         * 参数2：direct key
         * 参数3：其他属性
         * 参数4：消息体
         * RabbitMQ默认有一个exchange，叫default exchange，它用一个空字符串，它是direct exchange类型
         * 表示任何发送这个exchange的消息都会被路由到routing key的名字对应的队列上，如果没有对应的队列，则消息会被丢弃
         *
         */

        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.err.println("【simple send】"+message+"【。。。。】");
        //关闭
        channel.close();
        connectiont.close();

    }
}
