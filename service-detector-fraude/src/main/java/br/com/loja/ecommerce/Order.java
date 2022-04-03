package br.com.loja.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String orderId;
    private final BigDecimal valor;
    private final String email;

    public Order(String orderId, BigDecimal valor, String email) {
        this.orderId = orderId;
        this.valor = valor;
        this.email = email;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", valor=" + valor +
                ", email='" + email + '\'' +
                '}';
    }
}
