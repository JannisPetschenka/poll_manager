package no.hvl.poll_manager.repository;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;

@Component
public class PollMessageListener implements ChannelAwareMessageListener {

	@Autowired
	private PollManager pollManager;
	private Gson gson;

	public PollMessageListener() {
		this.gson = new Gson();
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String messageBody = new String(message.getBody());

		if (messageBody.matches("^New poll created with ID: \\d+$") || messageBody.startsWith("New vote on poll: ")) {
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			return;
		}
		try {
			PollMessage m = gson.fromJson(messageBody, PollMessage.class);
			if (m != null) {
				pollManager.addVoteForPollAnonymous(m.pollId, m.caption);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private record PollMessage(Integer pollId, String caption) {
	}
}
