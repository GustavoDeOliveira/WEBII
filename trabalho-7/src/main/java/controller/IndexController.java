/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Persistence;
import modelo.Usuario;
import persistencia.UsuarioDAO;
import spark.ModelAndView;

public class IndexController extends Controller {

    UsuarioDAO dao;

    public IndexController() {
        this.dao = new UsuarioDAO(Persistence.createEntityManagerFactory("default"));
    }

    public ModelAndView index() {
        Map map = new HashMap();
        int id = request.session().attribute("usuario") != null ? request.session().attribute("usuario") : 0;
        Usuario usuario = dao.findUsuario(id);
        if (usuario != null) {
            response.redirect("/jogador");
            return null;
        } else {
            request.session().attribute("usuario", 0);
            request.session().attribute("logado", false);
        }
        map.put("usuario", request.attribute("usuario"));
        map.put("logado", request.attribute("logado"));
        return new ModelAndView(map, "index.mustache");
    }

    public void login() {
        String login = request.queryMap().get("login").value();
        String senha = request.queryMap().get("senha").value();
        Usuario usuario = dao.findUsuario(login, senha);
        if (usuario != null) {
            request.session().attribute("usuario", usuario.getId());
            response.redirect("/jogador");
        } else {
            response.redirect("/");
        }
    }

    public void logout() {
        request.session().attribute("usuario", 0);
        response.redirect("/");
    }

    public Usuario usuario(int id) {
        return dao.findUsuario(id);
    }
}
