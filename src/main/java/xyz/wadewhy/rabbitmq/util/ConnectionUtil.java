package xyz.wadewhy.rabbitmq.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 作者博客：wadewhy.xyz
 *
 * @Classname ConnectionUtil
 * @Description TODO
 * @Date 20-4-21 下午9:54
 * @Created by wadewhy
 */
public class ConnectionUtil {
    /**
     * @Author wadewhy
     * @Description
     * @Date 20-4-21 下午10:12
     * @Param
     * @return
     **/
    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址
        factory.setHost("192.168.81.128");
        // AMQP 5672协议
        factory.setPort(5672);
        // vhost
        factory.setVirtualHost("/vhost_wadewhy");
        // 用户名
        factory.setUsername("wadewhy");
        // 密码
        factory.setPassword("wadewhy");
        return factory.newConnection();

    }

}
