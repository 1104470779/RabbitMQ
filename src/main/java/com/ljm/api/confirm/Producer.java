package com.ljm.api.confirm;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
	private static String exchangeName = "test_confirm_exchange";
	private static String routingKey = "confirm.test";
	
	public static void main(String[] args) throws Exception {
		send();
	}

	private static void send() throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(Global.HOST);
		connectionFactory.setPort(Global.PORT);
		connectionFactory.setVirtualHost("/");
		//获取connection
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		//指定消息确认模式
		channel.confirmSelect();
		String msg="hello word confirm";
		//发送
		channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
		SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
		//确认监听
		channel.addConfirmListener(new ConfirmListener() {
			/**
			 *	确认（成功）
			 * @param deliveryTag 消息唯一标签
			 * @param multiple
			 * @throws IOException
			 */
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("ack");
				if (multiple) {
					confirmSet.headSet(deliveryTag + 1L).clear();
				} else {
					confirmSet.remove(deliveryTag);
				}
			}

			/**
			 * 非确认（失败）
			 * @param deliveryTag
			 * @param multiple
			 * @throws IOException
			 */
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("nack");
			}
		});
	}
}
