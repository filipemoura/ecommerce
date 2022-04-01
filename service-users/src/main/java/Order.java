import java.math.BigDecimal;

public class Order {

    private final String userId, orderId;
    private final BigDecimal valor;
    private final String email;

    public Order(String userId, String orderId, BigDecimal valor, String email) {
        this.userId = userId;
        this.orderId = orderId;
        this.valor = valor;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
