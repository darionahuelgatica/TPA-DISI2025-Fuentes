package ar.edu.utn.dds.k3003.amqp.publishers;

import ar.edu.utn.dds.k3003.dataaccess.model.Hecho;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.MessageDeliveryMode;
import java.util.HashMap;
import java.util.Map;

@Component
public class HechosUpdatesQueuePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;
    @Value("${fuente.id}")
    private String fuenteId;

    public HechosUpdatesQueuePublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.hechos.updates.queue}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public void publishUpsert(Hecho hecho) {
        Map<String, Object> payload = new HashMap<>();
        PayloadPutIfNotNull(payload, "hechoId", hecho.getId());
        PayloadPutIfNotNull(payload,"fuenteId", fuenteId);
        PayloadPutIfNotNull(payload,"nombreColeccion", hecho.getColeccionId());
        PayloadPutIfNotNull(payload,"titulo", hecho.getTitulo());

        Publish("upsert", payload);
    }

    public void publishDelete(String hechoId) {
        Map<String, Object> payload = Map.of("hechoId", hechoId, "fuenteId", fuenteId);
        Publish("delete", payload);
    }

    public void publishDeleteAll() {
        Map<String, Object> payload = Map.of("fuenteId", fuenteId);
        Publish("deleteAll", payload);
    }


    private void PayloadPutIfNotNull(Map<String, Object> payload, String propName, String propValue) {
        if (propValue != null) {
            payload.put(propName, propValue);
        }
    }

    private void Publish(String op, Map<String, Object> payload) {

        rabbitTemplate.convertAndSend("", queueName, payload, msg -> {
            msg.getMessageProperties().setHeader("op", op);
            msg.getMessageProperties().setContentType("application/json");
            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return msg;
        });
    }
}


//
//@Component
//public class HechosUpdatesQueuePublisher {
//
//    private final RabbitTemplate rabbitTemplate;
//    private final String upsertedHechosQueue;
//
//    public HechosUpdatesQueuePublisher(
//            RabbitTemplate rabbitTemplate,
//            @Value("${app.hechos.updates.queue}") String queueName) {
//        this.rabbitTemplate = rabbitTemplate;
//        this.upsertedHechosQueue = queueName;
//    }
//
//    public void publishUpsertToQueue(Hecho hecho) {
//        String parsedMessage;
//
//
//        rabbitTemplate.convertAndSend("", upsertedHechosQueue, parsedMessage);
//    }
//}