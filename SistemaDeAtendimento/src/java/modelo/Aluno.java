/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;
import javax.persistence.*;

/**
 *
 * @author gustavo
 */
@Entity
@Table(name = "aluno")
public class Aluno extends Pessoa {

    @Column(unique = true)
    private String matricula;
    
    @OneToMany(mappedBy = "aluno", cascade = {CascadeType.REMOVE})
    private List<Atendimento> atendimentos;

    
    public Aluno() {
    }

    
    public List<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
