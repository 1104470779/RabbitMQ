package com.ljm.exchange.topic;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * topic exchange
 * 模糊匹配 #多个 * 一个
 */
public class ConsumerTopicExchange {

    private static String exchangeName = "test_topic_exchange";
    private static String exchangeType = "topic";
    private static String queueName = "test_topic_queue";
    //#  *
    private static String routingKey = "user.*";
    public static void main(String[] args) throws Exception {
        consumer();
    }

    private static void consumer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(Global.HOST);
        connectionFactory.setPort(Global.PORT);
        connectionFactory.setVirtualHost("/");
        //重连机制
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // 1 声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 2 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 3 建立交换机和队列的绑定关系:
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        //循环获取消息
        while (true) {
            //获取消息，如果没有消息，这一步将会一直阻塞
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费消息：" + msg);
        }
    }
}
