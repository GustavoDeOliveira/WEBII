package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "posicao")
public class Posicao extends Entidade {
    
    @Column(nullable = false, unique = true)
    private String descricao;

    public Posicao() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
}
