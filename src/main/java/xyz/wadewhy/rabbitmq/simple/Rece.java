package xyz.wadewhy.rabbitmq.simple;

import com.rabbitmq.client.*;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import javax.print.attribute.standard.QueuedJobCount;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *  耦合性高
 * @Classname Rece1
 * @Description TODO 接收服务
 * @Date 20-4-21 下午10:28
 * @Created by wadewhy
 */
public class Rece {
    //队列名
    private final static String QUEUE_NAME = "simple_rabbitmq";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        newMethod();
        return;
    }

    /**
     * @return
     * @Author wadewhy
     * @Description TODO 新api推荐的方法
     * @Date 20-4-21 下午10:58
     * @Param
     **/
    private static void newMethod() throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //在通道里面声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        /**
         * 一旦有消息就会出发这个事件handleDelivery，事件触发模型
         */
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.err.println("-----新接受方法接受消息-------：" + message);
            }
        };
        /**
         * 监听队列
         * 参数1：队列名称
         * 参数2：是否发送ack包，不发送ack消息会持续再服务端保存，知道收到ack
         *       可以通过channel。basicAck手动回复ack。
         * 参数3：消费者
         */
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    /**
     * @return
     * @Author wadewhy
     * @Description TODO 老的接受服务的方法
     * @Date 20-4-21 下午10:40
     * @Param
     **/
    private static void old() throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道channel
        Channel channel = connection.createChannel();
        //定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            //读取
            String message = new String(delivery.getBody());
            System.err.println("-------读取-------：" + message);
        }
    }
}
