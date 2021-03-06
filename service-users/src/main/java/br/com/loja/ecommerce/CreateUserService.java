package br.com.loja.ecommerce;

import br.com.loja.ecommerce.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table Users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200))");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        var userService = new CreateUserService();
        try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(),
                "ECOMMERCE_NOVO_PEDIDO",
                userService::parse,
                Order.class,
                new HashMap<>())) {
            service.run();
        };
    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("-------------------------------------");
        System.out.println("Processando novo pedido, checando por novo usuário");
        System.out.println(record.value());

        var order = record.value();
        if (isNovoUsuario(order.getEmail())) {
            insertNovoUsuario(order.getEmail());
        }

    }

    private void insertNovoUsuario(String email) throws SQLException {
        var insert = connection.prepareStatement("insert into Users (uuid, email) " +
                "values (?, ?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
        System.out.println("Usuário " + email + " adicionado.");
        System.out.println();
    }

    private boolean isNovoUsuario(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from Users " +
                "where email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        return !results.next();
    }
}
