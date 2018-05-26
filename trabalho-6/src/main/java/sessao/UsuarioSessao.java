/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessao;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import modelo.Usuario;

/**
 *
 * @author iapereira
 */
@SessionScoped
public class UsuarioSessao implements Serializable {

    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
