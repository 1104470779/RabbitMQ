package com.ljm.returnlistener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.ljm.Global;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Producer {


	private static String exchange = "test_return_exchange";
	private static String routingKey = "return.save";
	private static String routingKeyError = "abc.save";

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

		String msg = "Hello RabbitMQ Return Message";
		channel.addReturnListener(new ReturnListener() {
			//不可达消息处理

			/**
			 *
			 * @param replyCode 响应码（成功失败）
			 * @param replyText 文本
			 * @param exchange 路由交换机
			 * @param routingKey
			 * @param properties
			 * @param body
			 * @throws IOException
			 */
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange,
									 String routingKey, BasicProperties properties, byte[] body) throws IOException {
				System.err.println("---------handle  return----------");
				System.err.println("replyCode: " + replyCode);
				System.err.println("replyText: " + replyText);
				System.err.println("exchange: " + exchange);
				System.err.println("routingKey: " + routingKey);
				System.err.println("properties: " + properties);
				System.err.println("body: " + new String(body));
			}
		});
		//handleReturn 必须设置 mandatory=true
		channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
	}
}
