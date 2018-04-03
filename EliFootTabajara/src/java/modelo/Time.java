/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author gustavo
 */
@Entity
public class Time extends Entidade {

    @OneToMany(mappedBy = "time", orphanRemoval = true, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Jogador> jogadores;
    
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST})
    private Tecnico tecnico;
    
    @Column(unique = true)
    private String nome;

    public Time() {
    }

    public Time(String nome) {
        this.nome = nome;
        this.jogadores = new ArrayList<>();
    }
    
    
    public void addJogador(Jogador j){
        jogadores.add(j);
    }
    
    
    public void setJogador(int i, Jogador j) {
        try {
            this.jogadores.set(i, j);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    public Jogador getJogador(int i, Jogador j) {
        try {
            return this.jogadores.get(i);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    
    public List<Jogador> getJogadores() {
        return jogadores;
    }

    
    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
        
}
