/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javax.persistence.*;

/**
 *
 * @author gustavo
 */
@Entity
public abstract class Pessoa extends Entidade {
    
    @Column(nullable = false)
    private String nome;

    public Pessoa() {
    }
    
    public Pessoa(String nome) {
        if (nome == null || nome.equals("")) {
            throw new IllegalArgumentException("A pessoa deve ter um nome.");
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
