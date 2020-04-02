package com.ljm.api.message;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Consumer {
	private static String queue = "test-001";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		consume();
	}

	/**
	 * 未指定exchange  queue会匹配routingKey
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public static void consume() throws IOException, TimeoutException, InterruptedException {
		//ConnectionFactory配置
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(Global.HOST);
		connectionFactory.setPort(Global.PORT);
		connectionFactory.setVirtualHost("/");
		//创建连接
		Connection connection = connectionFactory.newConnection();
		//创建信道
		Channel channel = connection.createChannel();
		//声明队列
		channel.queueDeclare(queue, true, false, false, null);
		//创建消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//设置channel
		channel.basicConsume(queue, true, consumer);
		while (true){
			//获取消息
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("消费消息："+msg);
			Map<String, Object> headers = delivery.getProperties().getHeaders();
			System.err.println("headers get my1 value: " + headers.get("my1"));
		}
	}
}
