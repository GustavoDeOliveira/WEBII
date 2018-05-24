/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;


@Entity
@Table(name = "aluno")
public class Aluno implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    
    @NotNull(message = "O nome não pode ser vazio.")
    @Size(min=1, max=10, message = "O nome não pode ser vazio e não pode ter mais de 10 caractéres.") 
    @Column(length = 10, nullable = false)
    private String nome;
    
    @NotNull(message = "O cpf deve ser preenchido.")
    @Size(min=11, max=11, message = "O cpf deve conter exatamente 11 dígitos, sem pontos nem traços.")
    @Pattern(regexp = "^\\d+", message = "O cpf deve ser preenchido apenas com números.")
    @Column(length = 11, nullable = false)
    private String cpf;
    
    @NotNull(message = "O email é obrigatório.")
    @Pattern(regexp = "\\S+@S+\\.S+$", message = "Email inválido.")
    @Column(nullable = false)
    private String email;
    
    @Column
    private String senha;

    @Past
    @Column(columnDefinition = "ultimo_pagamento")
    private Date ultimoPagamento;
    
    @Column(columnDefinition = "prazo_pagamento")
    private Date prazoPagamento;
    
    @ManyToMany(mappedBy = "atividade")
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
    
}