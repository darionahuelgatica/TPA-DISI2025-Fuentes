package ar.edu.utn.dds.k3003.presentacion.amqp;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AmqpConfig {

    public static String exchangeName;

    public AmqpConfig(@Value("${app.hechos.new.fanout}") String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Bean
    public FanoutExchange newHechosExchange() {
        return new FanoutExchange(exchangeName, true, false);
    }

    @Bean
    public Queue newHechosQueuePerInstance() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding newHechosBinding(FanoutExchange newHechosExchange, Queue newHechosQueuePerInstance) {
        return BindingBuilder.bind(newHechosQueuePerInstance).to(newHechosExchange);
    }
}