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
public class Tecnico extends Pessoa {

    @OneToOne(optional = false, cascade = CascadeType.REMOVE)
    private Time time;

    public Tecnico() {
    }

    public Tecnico(String nome, Time time) {
        super(nome);
        if(time == null)
            throw new IllegalArgumentException("O técnico deve estar em um time.");
        
        this.time = time;
    }


    public Time getTime() {
        return time;
    }

    
    public void setTime(Time time) {
        if(time == null)
            throw new IllegalArgumentException("O técnico deve estar em um time.");
        
        this.time = time;
    }
}
