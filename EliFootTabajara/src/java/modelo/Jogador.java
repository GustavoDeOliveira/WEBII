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
@NamedQuery(name = "findJogadoresWithIds", query = "SELECT j FROM Jogador j WHERE j.id IN (:ids)")
public class Jogador extends Pessoa {

    @JoinColumn(name="time_id", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    private Time time;

    public Jogador() {
    }

    public Jogador(String nome, Time time) {
        super(nome);
        if(time == null)
            throw new IllegalArgumentException("O jogador deve ter um time.");
        
        this.time = time;
    }


    public Time getTime() {
        return time;
    }

    
    public void setTime(Time time) {
        if(time == null)
            throw new IllegalArgumentException("O jogador deve ter um time.");
        
        this.time = time;
    }
}
