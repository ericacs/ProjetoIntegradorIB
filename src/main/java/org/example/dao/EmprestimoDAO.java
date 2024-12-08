package org.example.dao;

import org.example.model.Emprestimo;
import org.example.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    private Connection connection;

    public EmprestimoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO Emprestimo (data_emp, data_dev, id_livro, id_usuario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(emprestimo.getDataEmp()));
            stmt.setDate(2, Date.valueOf(emprestimo.getDataDev()));
            stmt.setInt(3, emprestimo.getIdLivro());
            stmt.setInt(4, emprestimo.getIdUsuario());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emprestimo.setIdEmprestimo(generatedKeys.getInt(1));
                } else {
                    System.out.println("Nenhum ID foi gerado.");
                }
            }
        }
    }

    public Emprestimo getPorId(int idEmprestimo) throws SQLException {
        String sql = "SELECT * FROM Emprestimo where id_emprestimo=" + idEmprestimo;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setIdEmprestimo(rs.getInt("id_emprestimo"));
                emprestimo.setDataEmp(rs.getDate("data_emp").toLocalDate());
                emprestimo.setDataDev(rs.getDate("data_dev").toLocalDate());
                emprestimo.setIdLivro(rs.getInt("id_livro"));
                emprestimo.setIdUsuario(rs.getInt("id_usuario"));
                return emprestimo;
            }
        }
        return null;
    }

    public List<Emprestimo> listar() throws SQLException {
        //List eh um elemento de estrutura de dados
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM Emprestimo";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setIdEmprestimo(rs.getInt("id_emprestimo"));
                emprestimo.setDataEmp(rs.getDate("data_emp").toLocalDate());
                emprestimo.setDataDev(rs.getDate("data_dev").toLocalDate());
                emprestimo.setIdLivro(rs.getInt("id_livro"));
                emprestimo.setIdUsuario(rs.getInt("id_usuario"));
                emprestimos.add(emprestimo);
            }
        }
        return emprestimos;
    }

    public void atualizar(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE Emprestimo SET data_emp = ?, data_dev = ?, id_livro = ?, id_usuario = ? WHERE id_emprestimo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(emprestimo.getDataEmp()));
            stmt.setDate(2, Date.valueOf(emprestimo.getDataDev()));
            stmt.setInt(3, emprestimo.getIdLivro());
            stmt.setInt(4, emprestimo.getIdUsuario());
            stmt.setInt(5, emprestimo.getIdEmprestimo());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idEmprestimo) throws SQLException {
        String sql = "DELETE FROM Emprestimo WHERE id_emprestimo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmprestimo);
            stmt.executeUpdate();
        }
    }
}
