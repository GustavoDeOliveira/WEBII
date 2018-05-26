/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

@Entity(name = "Aluno")
@Table(name = "aluno")
public class Aluno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull(message = "O nome não pode ser vazio.")
    @Size(min = 1, max = 10, message = "O nome não pode ser vazio e não pode ter mais de 10 caractéres.")
    @Column(length = 10, nullable = false)
    private String nome;

    @NotNull(message = "O cpf deve ser preenchido.")
    @Size(min = 11, max = 11, message = "O cpf deve conter exatamente 11 dígitos, sem pontos nem traços.")
    @Pattern(regexp = "^\\d+", message = "O cpf deve ser preenchido apenas com números.")
    @Column(length = 11, nullable = false)
    private String cpf;

    @NotNull(message = "O email é obrigatório.")
    @Pattern(regexp = "\\S+@\\S+\\.\\S+$", message = "Email inválido.")
    @Column(nullable = false)
    private String email;

    @Column
    private String senha;

    @Past
    @Column(name = "ultimo_pagamento")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date ultimoPagamento;

    @Column(name = "prazo_pagamento")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date prazoPagamento;

    @ManyToMany(mappedBy = "alunos")
    private List<Atividade> atividades;

    public Aluno() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getUltimoPagamento() {
        return ultimoPagamento;
    }

    public void setUltimoPagamento(Date ultimoPagamento) {
        this.ultimoPagamento = ultimoPagamento;
    }

    public Date getPrazoPagamento() {
        return prazoPagamento;
    }

    public void setPrazoPagamento(Date prazoPagamento) {
        this.prazoPagamento = prazoPagamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + this.id;
        hash = 13 * hash + Objects.hashCode(this.cpf);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aluno other = (Aluno) obj;
        return this.id == other.id;
    }

}
