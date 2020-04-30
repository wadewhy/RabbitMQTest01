package xyz.wadewhy.rabbitmq.confirm2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import xyz.wadewhy.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname Send
 * @Description TODO 异步处理confirm
 * @Date 20-4-24 下午8:42
 * @Created by wadewhy
 */
public class Send {
    //队列名
    private final  static String QUEUE_NAME="queue_name_asy";

    public static void main(final String[] args) throws IOException, TimeoutException {
        //创建连接和channel
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //开启confirm事务;
        channel.confirmSelect();
        //创建set
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        //监听事务
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                if (b){
                    System.err.println("handleNack: "+l);
                    confirmSet.headSet(l+1).clear();
                }else {
                    System.err.println("handleAck:"+b);
                    confirmSet.remove(l);
                }
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {

            }
        });
    }
}
