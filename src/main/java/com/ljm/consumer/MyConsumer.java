package com.ljm.consumer;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 自定义消费者
 */
public class MyConsumer extends DefaultConsumer {


	public MyConsumer(Channel channel) {
		super(channel);
	}

	/**
	 *
	 * @param consumerTag 消费标签
	 * @param envelope 关键信息
	 * @param properties
	 * @param body
	 * @throws IOException
	 */
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		System.err.println("-----------consume message----------");
		System.err.println("consumerTag: " + consumerTag);
		System.err.println("envelope: " + envelope);
		System.err.println("properties: " + properties);
		System.err.println("body: " + new String(body));
	}


}
