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
@Table(name = "jogador")
@NamedQuery(name = "findJogadoresByPosicaoId", query = "SELECT j FROM Jogador j WHERE j.posicao.id = :posicaoId")
public class Jogador extends Pessoa {

    @ManyToOne(optional = false, cascade = {CascadeType.REMOVE})
    @JoinColumn(name="equipe_id", referencedColumnName = "id")
    private Equipe equipe;
    
    @OneToOne(optional = false)
    @JoinColumn(name="posicao_id", referencedColumnName = "id")
    Posicao posicao;

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public Jogador() {
    }

    @Deprecated
    public Jogador(String nome, Equipe time) {
        super(nome);
        this.equipe = time;
    }


    public Equipe getEquipe() {
        return equipe;
    }

    
    public void setEquipe(Equipe time) {
        if(time == null)
            throw new IllegalArgumentException("O jogador deve ter um time.");
        
        this.equipe = time;
    }
}
