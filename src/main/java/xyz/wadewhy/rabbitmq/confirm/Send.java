package xyz.wadewhy.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Send
 * @Description TODO confirm机制处理消息反馈
 * @Date 20-4-24 下午2:32
 * @Created by wadewhy
 */
public class Send {
    private final  static String QUEUE_NAME = "queue_name_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //创建连接，channel
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //消息
        String message = "confirm message";
        String msg=null;
        channel.confirmSelect();
        final long start = System.currentTimeMillis();
        for (int i = 0; i <5 ; i++) {
            //模拟出错
            int error = 1/(i-3);
             msg= "第"+i+"条"+message;
            //发送
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            //单个Confirm
            if (channel.waitForConfirms()) {//  消费者确认消费
                System.err.println("发送成功"+msg);
            }else{
                // 进行消息重发
            }
        }
        /*//批量Confirm模式
        if (channel.waitForConfirms()) {//  消费者确认消费
            System.err.println("发送成功"+msg);
        }else{
            // 进行消息重发
        }*/
        System.out.println("执行waitForConfirms耗费时间: " + (System.currentTimeMillis() - start) + "ms");
        //关闭资源
        channel.close();
        connection.close();
    }
}
