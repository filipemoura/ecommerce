package br.com.loja.ecommerce;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NovoPedidoServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var email = req.getParameter("email");
            var valor = new BigDecimal(req.getParameter("valor"));

            var orderId = UUID.randomUUID().toString();

            var order = new Order(orderId, valor, email);
            orderDispatcher.send("ECOMMERCE_NOVO_PEDIDO", email, order);

            var emailBody = "Obrigado por seu pedido! Nós estamos processando!";
            emailDispatcher.send("ECOMMERCE_ENVIAR_EMAIL", email, emailBody);

            System.out.println("Processo do novo pedido terminado.");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Novo pedido enviado.");
        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }
}
