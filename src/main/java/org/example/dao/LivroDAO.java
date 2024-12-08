package org.example.dao;

import org.example.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    private Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Livro livro) throws SQLException {
        String sql = "INSERT INTO Livro (titulo, autor, genero, qtde_estoque, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getGenero());
            stmt.setInt(4, livro.getQtdeEstoque());
            stmt.setInt(5, livro.getIdCategoria());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livro.setIdLivro(generatedKeys.getInt(1));
                } else {
                    System.out.println("Nenhum ID foi gerado.");
                }
            }
        }
    }

    public Livro getPorId(int idLivro) throws SQLException {
        String sql = "SELECT * FROM Livro where id_livro = " + idLivro;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setIdLivro(rs.getInt("id_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setGenero(rs.getString("genero"));
                livro.setQtdeEstoque(rs.getInt("qtde_estoque"));
                livro.setIdCategoria(rs.getInt("id_categoria"));

                return livro;
            }
        }
        return null;
    }

    public List<Livro> listar() throws SQLException {
        //List eh um elemento de estrutura de dados
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM Livro";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setIdLivro(rs.getInt("id_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setGenero(rs.getString("genero"));
                livro.setQtdeEstoque(rs.getInt("qtde_estoque"));
                livro.setIdCategoria(rs.getInt("id_categoria"));
                livros.add(livro);
            }
        }
        return livros;
    }

    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE Livro SET titulo = ?, autor = ?, genero = ?, qtde_estoque = ?, id_categoria = ? WHERE id_livro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getGenero());
            stmt.setInt(4, livro.getQtdeEstoque());
            stmt.setInt(5, livro.getIdCategoria());
            stmt.setInt(6, livro.getIdLivro());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idLivro) throws SQLException {
        String sql = "DELETE FROM Livro WHERE id_livro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }
}
