/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author gustavo
 */
public class Jogador extends Entidade {

    private Time time;

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
