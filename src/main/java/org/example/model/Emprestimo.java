package org.example.model;

import java.time.LocalDate;

public class Emprestimo {
    private int idEmprestimo;
    private LocalDate dataEmp;
    private LocalDate dataDev;
    private int idLivro;
    private int idUsuario;

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public LocalDate getDataEmp() {
        return dataEmp;
    }

    public void setDataEmp(LocalDate dataEmp) {
        this.dataEmp = dataEmp;
    }

    public LocalDate getDataDev() {
        return dataDev;
    }

    public void setDataDev(LocalDate dataDev) {
        this.dataDev = dataDev;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

}
