package com.ljm.api.qos;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费端做消息限流
 */
public class Consumer {

	private static String exchangeName = "test_qos_exchange";
	private static String queueName = "test_qos_queue";
	private static String routingKey = "qos.#";

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
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);

		//1 限流方式  第一件事就是 autoAck设置为 false   大流量的场景下一定是手动签收
		//0不做消息大小限制  prefetchCount一次最多处理消息（设置为1，每次处理一条，处理完会反馈，再处理下一条）
        // global=false应用到consumer级别  true channel级别
		channel.basicQos(0, 1, false);
		channel.basicConsume(queueName, false, new MyConsumer(channel));
	}
}
