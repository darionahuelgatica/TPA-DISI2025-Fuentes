package ar.edu.utn.dds.k3003.presentacion.amqp.subscribers;

import ar.edu.utn.dds.k3003.business.services.IHechoService;
import ar.edu.utn.dds.k3003.presentacion.dtos.HechoDTO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NewHechoSubscriber {

    private final IHechoService hechoService;

    public NewHechoSubscriber(IHechoService hechoService) {
        this.hechoService = hechoService;
    }

    @RabbitListener(queues = "${app.amqp.queue}")
    public void onMessage(@Payload HechoDTO hechoDTO, Channel channel, Message message) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();

        try {
            this.hechoService.addHecho(hechoDTO);
            channel.basicAck(tag, false);
        } catch (Exception ex) {
            channel.basicNack(tag, false, true);
            throw ex;
        }
    }
}