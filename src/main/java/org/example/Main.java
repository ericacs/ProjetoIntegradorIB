package org.example;


import com.sun.net.httpserver.HttpServer;
import org.example.controller.CategoriaController;
import org.example.controller.EmprestimoController;
import org.example.controller.LivroController;
import org.example.controller.UsuarioController;
import org.example.dao.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    //TODO o programa começa aqui
    public static void main(String[] args) throws SQLException, IOException {

        //Conectanto no banco de dados MySQl
        Connection connection = DbConnection.getConnection();

        // Cria controles
        LivroController livroController = new LivroController(new LivroDAO(connection));
        UsuarioController usuarioController = new UsuarioController(new UsuarioDAO(connection));
        CategoriaController categoriaController = new CategoriaController(new CategoriaDAO(connection));
        EmprestimoController emprestimoController = new EmprestimoController(new EmprestimoDAO(connection));

        // Criar o servidor HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/livros", livroController::handle);
        server.createContext("/usuarios", usuarioController::handle);
        server.createContext("/categorias", categoriaController::handle);
        server.createContext("/emprestimos", emprestimoController::handle);
        server.start();

        System.out.println("Servidor iniciado na porta 8080");
    }
}