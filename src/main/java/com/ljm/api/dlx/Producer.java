package com.ljm.api.dlx;

import com.ljm.Global;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

	private static String exchange = "test_dlx_exchange";
	private static String routingKey = "dlx.save";

	public static void main(String[] args) throws Exception {
		send();
	}


	private static void send() throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(Global.HOST);
		connectionFactory.setPort(Global.PORT);
		connectionFactory.setVirtualHost("/");

		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();


		String msg = "Hello RabbitMQ DLX Message";

		for(int i =0; i<1; i ++){

			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.expiration("10000")
					.build();
			channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes());
		}
	}
}
