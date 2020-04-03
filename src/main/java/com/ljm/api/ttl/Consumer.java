package com.ljm.api.ttl;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static String exchangeName = "test_consumer_exchange";
    private static String routingKey = "consumer.#";
    private static String queueName = "test_consumer_queue";

    public static void main(String[] args) throws Exception {
        consumer();
    }

    private static void consumer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Global.HOST);
        connectionFactory.setPort(Global.PORT);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        Map<String, Object> argss=new HashMap<>();
        argss.put("x-message-ttl",6000);//队列的所有消息都有相同的过期时间 ，一旦消息过期就会从队列中抹去
        channel.queueDeclare(queueName, true, false, false, argss);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName, true, new MyConsumer(channel));
    }
}
