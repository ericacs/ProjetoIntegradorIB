package org.example.dao;

import org.example.model.Categoria;
import org.example.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private Connection connection;

    public CategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categoria.setIdCategoria(generatedKeys.getInt(1));
                } else {
                    System.out.println("Nenhum ID foi gerado.");
                }
            }
        }
    }

    public Categoria getPorId(int idCategoria) throws SQLException {
        String sql = "SELECT * FROM Categoria where id_categoria = " + idCategoria;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                return categoria;
            }
        }
        return null;
    }

    public List<Categoria> listar() throws SQLException {
        //List eh um elemento de estrutura de dados
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    public void atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE Categoria SET nome = ? WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getIdCategoria());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idCategoria) throws SQLException {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            stmt.executeUpdate();
        }
    }
}
