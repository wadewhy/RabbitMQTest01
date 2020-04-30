package xyz.wadewhy.rabbitmq.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Send
 * @Description TODO rabbitmq消息确认机制之事务
 * @Date 20-4-24 下午12:20
 * @Created by wadewhy
 */
public class Send {
    private final static String QUEUE_NAME="queue_name_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接和channel
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //消息
        String message = "queue_name_tx_mes";
        //事务
        try{
            /**
             * txSelect 将当前channel设置为transaction模式[事务模式]
             * txCommit 提交当前事务
             * txRollback 事务回滚
             */
            channel.txSelect();
            //模拟异常发生
            int error  = 1/0;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.err.println("-----send success-------"+message);
            channel.txCommit();
        }catch (Exception e){
            //回滚
            channel.txRollback();
            System.err.println("------send error------");
        }finally {
            channel.close();
            connection.close();

        }

    }
}
