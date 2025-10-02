package ar.edu.utn.dds.k3003.presentacion.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {


    @Value("${app.amqp.queue}")
    private String queueName;

    @Bean
    public Queue appQueue() {
        return QueueBuilder.nonDurable(queueName).build();
    }
}