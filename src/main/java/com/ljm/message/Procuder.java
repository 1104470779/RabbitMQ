package com.ljm.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.ljm.Global;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * message设置
 */
public class Procuder {

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

        Map<String, Object> headers = new HashMap<>();
        headers.put("my1", "111");
        headers.put("my2", "222");

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .headers(headers)
                .build();
        channel.basicPublish("", "test-001", properties, msg.getBytes());
        //关闭连接
        channel.close();
        connection.close();
    }

}
