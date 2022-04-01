package br.com.loja.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String userId, orderId;
    private final BigDecimal valor;

    public Order(String userId, String orderId, BigDecimal valor) {
        this.userId = userId;
        this.orderId = orderId;
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", valor=" + valor +
                '}';
    }
}
