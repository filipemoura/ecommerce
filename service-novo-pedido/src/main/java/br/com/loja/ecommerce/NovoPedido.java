package br.com.loja.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NovoPedido {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<String>()) {
                var email = Math.random() + "@email.com";
                for (var i = 0; i < 5; i++) {
                    var orderId = UUID.randomUUID().toString();
                    var value = new BigDecimal(Math.random() * 5000 + 1);

                    var order = new Order(orderId, value, email);
                    orderDispatcher.send("ECOMMERCE_NOVO_PEDIDO", email, order);

                    var emailBody = "Obrigado por seu pedido! Nós estamos processando!";
                    emailDispatcher.send("ECOMMERCE_ENVIAR_EMAIL", email, emailBody);
                }
            }
        }
    }
}
