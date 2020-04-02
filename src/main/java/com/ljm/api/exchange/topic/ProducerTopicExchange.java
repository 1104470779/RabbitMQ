package com.ljm.api.exchange.topic;

import com.ljm.Global;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ProducerTopicExchange {

    private static String exchangeName = "test_topic_exchange";

    private static List<String> routingKeys = new ArrayList<>();

    static {
        routingKeys.add("user.save");
        routingKeys.add("user.update");
        routingKeys.add("user.delete.a");
    }

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

		String msg = "hello word by topic send.....";
		channel.basicPublish(exchangeName, routingKeys.get(0), null, msg.getBytes());
		channel.basicPublish(exchangeName, routingKeys.get(1), null, msg.getBytes());
		channel.basicPublish(exchangeName, routingKeys.get(2), null, msg.getBytes());
		channel.close();
		connection.close();
	}

}
