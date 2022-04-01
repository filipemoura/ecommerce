package br.com.loja.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DetectorFraudeService {

    public static void main(String[] args) {
        var fraudeService = new DetectorFraudeService();
        try (var service = new KafkaService<>(DetectorFraudeService.class.getSimpleName(),
                "ECOMMERCE_NOVO_PEDIDO",
                fraudeService::parse,
                Order.class,
                new HashMap<>())) {
            service.run();
        };
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("-------------------------------------");
        System.out.println("Processando novo pedido, checando por fraudes");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var order = record.value();
        if (isFraude(order)) {
            System.out.println("Pedido é uma fraude!!! " + order);
            orderDispatcher.send("ECOMMERCE_PEDIDO_REJEITADO", order.getUserId(), order);
        } else {
            System.out.println("Pedido aprovado: " + order);
            orderDispatcher.send("ECOMMERCE_PEDIDO_APROVADO", order.getUserId(), order);
        }
    }

    private boolean isFraude(Order order) {
        return order.getValor().compareTo(new BigDecimal("3000")) >= 0;
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, DetectorFraudeService.class.getSimpleName());
        return properties;
    }
}
