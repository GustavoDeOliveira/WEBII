/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import javax.persistence.*;

/**
 *
 * @author gustavo
 */
@Entity
@Table(name = "atendimento")
@NamedQueries({
    @NamedQuery(name = "listarAtendimentosPorAluno", 
                query = "SELECT a FROM Atendimento a WHERE a.aluno.id = :alunoId"),
    @NamedQuery(name = "listarAtendimentosPorProfessor", 
                query = "SELECT a FROM Atendimento a WHERE a.professor.id = :professorId")
})
public class Atendimento extends Entidade {
    
    @Column(name = "data_inicio")
    private String dataInicio;
    
    @Column(name = "data_fim")
    private String dataFim;

    @ManyToOne
    @JoinColumn(name="atendimentoAluno", referencedColumnName = "id")
    private Aluno aluno;
    
    @ManyToOne
    @JoinColumn(name="atendimentoProfessor", referencedColumnName = "id")
    private Professor professor;
    

    public Atendimento() {
    }
    

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
    
    
}
