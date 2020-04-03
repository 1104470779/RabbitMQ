package com.ljm.api.exchange.direct;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 直连必须指明routingKey
 * 消费端根据routingKey绑定
 */
public class ConsumerDirectExchange {

    static String exchangeName = "test_direct_exchange";
    static String exchangeType = "direct";
    static String queueName = "test_direct_queue";
    static String routingKey = "test.direct";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        customer();
    }


    public static void customer() throws IOException, TimeoutException, InterruptedException {
        //ConnectionFactory配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Global.HOST);
        connectionFactory.setPort(Global.PORT);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        //创建连接
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //表示声明了一个交换机 交换机名称 类型 是否持久化  是否自动删除 是否是内置的 结构化参数
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        //表示声明了一个队列  队列名称 是否持久化 是否排他 是否自动删除
        channel.queueDeclare(queueName, false, false, false, null);
        //建立一个绑定关系:
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        //参数：队列名称、是否自动ACK、Consumer
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
