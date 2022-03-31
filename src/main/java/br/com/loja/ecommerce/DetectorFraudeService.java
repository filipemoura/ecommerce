package br.com.loja.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Properties;

public class DetectorFraudeService {

    public static void main(String[] args) {
        var fraudeService = new DetectorFraudeService();
        try (var service = new KafkaService<Order>(DetectorFraudeService.class.getSimpleName(),
                "ECOMMERCE_NOVO_PEDIDO",
                fraudeService::parse,
                Order.class)) {
            service.run();
        };
    }

    private void parse(ConsumerRecord<String, Order> record) {
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

        System.out.println("Pedido processado");
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, DetectorFraudeService.class.getSimpleName());
        return properties;
    }
}
