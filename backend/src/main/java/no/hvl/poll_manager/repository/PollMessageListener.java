package no.hvl.poll_manager.repository;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class PollMessageListener implements ChannelAwareMessageListener {

	@Autowired
	private PollManager pollManager;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String messageBody = new String(message.getBody());

		if (messageBody.matches("^New poll created with ID: \\d+$") || messageBody.startsWith("New vote on poll: ")) {
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			return;
		}
		String routingKey = message.getMessageProperties().getReceivedRoutingKey();

		if (routingKey.matches("^poll\\.\\d+$")) {
			Integer pollId = Integer.parseInt(routingKey.substring(5));
			pollManager.addVoteForPollAnonymous(pollId, messageBody);
		} else {
			System.out.println("Invalid routing key: " + routingKey);
		}
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}
}
