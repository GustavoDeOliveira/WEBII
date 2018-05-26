package modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "Usuario")
@Table(name = "usuario")
@NamedQuery(name = "findUsuarioByLoginAndPassword", query = "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    @NotNull(message = "O login não pode ser vazio.")
    private String login;

    @Column
    @NotNull(message = "A senha não pode ser vazia.")
    private String senha;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean temLoginESenha() {
        return login != null && !login.isEmpty()
                && senha != null && !senha.isEmpty();
    }

}
