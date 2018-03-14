package modelo;

import java.util.List;

public abstract class Entidade {
    
    private int id;
    private String nome;

    public Entidade(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public boolean persistente() {
        return this.id != 0;
    }
    
}
