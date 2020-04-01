package com.ljm.a_rabbitmq;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
@Component
public class Producer {


    public static void main(String[] args) throws IOException, TimeoutException {
        send();
    }


    public static void send() throws IOException, TimeoutException {
        //ConnectionFactory配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Global.HOST);
        connectionFactory.setPort(Global.PORT);
        connectionFactory.setVirtualHost("/");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        String msg = "hello word";
        channel.basicPublish("", "test-001", null, msg.getBytes());
        //关闭连接
        channel.close();
        connection.close();
    }


}
