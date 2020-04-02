package com.ljm.api.exchange.direct;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ProducerDirectExchange {

	static String exchangeName = "test_direct_exchange";
	static String routingKey = "test.direct";

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
		String msg = "hello word by direct send.....";
		channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
		//关闭连接
		channel.close();
		connection.close();
	}

	
}
