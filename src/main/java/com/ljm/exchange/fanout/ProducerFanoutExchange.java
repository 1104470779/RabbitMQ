package com.ljm.exchange.fanout;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerFanoutExchange {

	private static String exchangeName = "test_fanout_exchange";
	
	public static void main(String[] args) throws Exception {
		send();
	}

	private static void send() throws IOException, TimeoutException {
		//1 创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(Global.HOST);
		connectionFactory.setPort(Global.PORT);
		connectionFactory.setVirtualHost("/");

		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		for(int i = 0; i < 10; i ++) {
			String msg = "hello word by fanout send.....";
			channel.basicPublish(exchangeName, "", null , msg.getBytes());
		}
		channel.close();
		connection.close();
	}

}
