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
@Table(name = "professor")
public class Professor extends Pessoa {
    
    @Column(unique = true)
    private String siape;
    
    @OneToMany(mappedBy = "professor", cascade = {CascadeType.REMOVE})
    private List<Atendimento> atendimentos;

    
    public Professor() {
    }
    

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }
    
    public List<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
    }
    
}
