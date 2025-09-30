package no.hvl.poll_manager.repository;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class PollManagerRabbit {

	@Bean
	public Exchange fanoutExchange() {
		return new FanoutExchange("pollsExchange");
    }

    @Bean
    public Queue pollQueue() {
        return new Queue("pollsQueue", true); 
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(pollQueue()).to(fanoutExchange()).with("").noargs();
																	 
    }

	@Bean
	public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
			PollMessageListener pollMessageListener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(pollQueue());
		container.setMessageListener(pollMessageListener);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		return container;
	}
}
