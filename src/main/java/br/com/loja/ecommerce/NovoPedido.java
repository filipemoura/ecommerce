package br.com.loja.ecommerce;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NovoPedido {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var dispatcher = new KafkaDispatcher()) {
            for (var i = 0; i < 5; i++) {
                var key = UUID.randomUUID().toString();

                var value = key + ", 2, 3";
                dispatcher.send("ECOMMERCE_NOVO_PEDIDO", key, value);

                var email = "Obrigado por seu pedido! Nós estamos processando!";
                dispatcher.send("ECOMMERCE_ENVIAR_EMAIL", key, email);
            }
        }
    }
}
