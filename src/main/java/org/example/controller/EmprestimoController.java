package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import org.example.dao.EmprestimoDAO;
import org.example.dao.UsuarioDAO;
import org.example.model.Emprestimo;
import org.example.model.Livro;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmprestimoController {
    private EmprestimoDAO dao;
    private ObjectMapper om;

    public EmprestimoController(EmprestimoDAO emprestimoDAO) {
        this.dao = emprestimoDAO;
        this.om = new ObjectMapper();
        this.om.registerModule(new JavaTimeModule()); // Desabilitando a falha no caso de datas vazias
        this.om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        String method = exchange.getRequestMethod();
        if (Pattern.matches("/emprestimos/\\d+", path)) {
            if (method.equals("DELETE")) {
                handleDeleteRequest(exchange, Integer.parseInt(path.split("/")[2]));
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        } else if (method.equals("GET")) {
            handleGetRequest(exchange);
        } else if (method.equals("POST")) {
            handlePostRequest(exchange);
        } else if (method.equals("PUT")) {
            handleUpdateRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed }
        }
    }

    private void handleUpdateRequest(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            Emprestimo emprestimo = om.readValue(inputStream, Emprestimo.class);

            Emprestimo emprestimoSaved = dao.getPorId(emprestimo.getIdLivro());

            String response;
            if (emprestimoSaved != null) {
                emprestimoSaved.setDataDev(emprestimo.getDataDev());
                emprestimoSaved.setDataEmp(emprestimo.getDataEmp());
                emprestimoSaved.setIdLivro(emprestimo.getIdLivro());
                emprestimoSaved.setIdUsuario(emprestimo.getIdUsuario());

                dao.atualizar(emprestimoSaved);
                response = "Emprestimo alterado com sucesso!";
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length); // 200 OK
            } else {
                response = "Emprestimo não econtrado!";
                exchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length); // 404 Not Found

            }
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (SQLException ex) {
            exchange.sendResponseHeaders(400, ex.getMessage().length());
            OutputStream os = exchange.getResponseBody();
            os.write(ex.getMessage().getBytes());
            os.close();
        }

    }

    private void handleDeleteRequest(HttpExchange exchange, int id) throws IOException {
        try {
            String response = "Emprestimo excluído com sucesso!";
            dao.excluir(id);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (SQLException ex) {
            exchange.sendResponseHeaders(400, ex.getMessage().length());
            OutputStream os = exchange.getResponseBody();
            os.write(ex.getMessage().getBytes());
            os.close();
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        try {
            List<Emprestimo> emprestimos = dao.listar();
            String response = om.writeValueAsString(emprestimos);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (SQLException ex) {
            exchange.sendResponseHeaders(400, ex.getMessage().length());
            OutputStream os = exchange.getResponseBody();
            os.write(ex.getMessage().getBytes());
            os.close();
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            Emprestimo emprestimo = om.readValue(inputStream, Emprestimo.class);
            dao.salvar(emprestimo);
            String response = om.writeValueAsString(emprestimo);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (SQLException ex) {
            exchange.sendResponseHeaders(400, ex.getMessage().length());
            OutputStream os = exchange.getResponseBody();
            os.write(ex.getMessage().getBytes());
            os.close();
        }
    }
}
