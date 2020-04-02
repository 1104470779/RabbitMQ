package com.ljm.api.confirm;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

	private static String exchangeName = "test_confirm_exchange";
	private static String routingKey = "confirm.#";
	private static String queueName = "test_confirm_queue";

	public static void main(String[] args) throws Exception {
		consumer();
	}

	private static void consumer() throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(Global.HOST);
		connectionFactory.setPort(Global.PORT);
		connectionFactory.setVirtualHost("/");

		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		//声明交换机和队列 然后进行绑定设置, 最后制定路由Key
		channel.exchangeDeclare(exchangeName, "topic", true);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);

		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		//autoAck自动签收
		channel.basicConsume(queueName, true, queueingConsumer);

		while(true){
			Delivery delivery = queueingConsumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("消费消息: " + msg);
		}
	}
}
