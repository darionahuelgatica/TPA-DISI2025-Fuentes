package ar.edu.utn.dds.k3003.presentacion.amqp.subscribers;
import ar.edu.utn.dds.k3003.business.services.IHechoService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class HechoSubscriber {

    private final IHechoService hechoService;

    public HechoSubscriber(IHechoService hechoService) {
        this.hechoService = hechoService;
    }

    @RabbitListener(queues = "${app.amqp.queue}")
    public void onMessage(Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            System.out.println("se recibio el siguiente payload:");
            System.out.println(payload);

            channel.basicAck(tag, false);

        } catch (Exception ex) {
            channel.basicNack(tag, false, true);
            throw ex;
        }
    }
}