/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author iapereira
 */
import controller.EquipeController;
import controller.GrupoController;
import controller.IndexController;
import controller.JogadorController;
import modelo.Usuario;
import spark.Request;
import spark.Response;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import spark.template.mustache.MustacheTemplateEngine;

public class Main {

    public static void main(String[] args) {

        staticFiles.location("/public"); // Static files
        
        IndexController indexController = new IndexController();
        GrupoController grupoController = new GrupoController();
        EquipeController equipeController = new EquipeController();
        JogadorController jogadorController = new JogadorController();

        before((Request rq, Response rs) -> {
            int id = rq.session().attribute("usuario") != null ? rq.session().attribute("usuario") : 0;
            boolean logado = rq.session() != null && id != 0;
            rq.attribute("logado", logado);
            if (logado) {
                indexController.setRequest(rq);
                indexController.setResponse(rs);
                Usuario u = indexController.usuario(id);
                rq.attribute("usuario", u);
            }
            if (!"/logar".equals(rq.uri()) && !"/".equals(rq.uri())) {
                if (!logado) {
                    rs.redirect("/");
                }
            }
        });
        
        
        // LOGIN
        get("/", (rq, rs) -> {
            indexController.setRequest(rq);
            indexController.setResponse(rs);
            return indexController.index();
        }, new MustacheTemplateEngine());
        
        get("/deslogar", (rq, rs) -> {
            indexController.setRequest(rq);
            indexController.setResponse(rs);
            indexController.logout();
            return null;
        });
        
        post("/logar", (rq, rs) -> {
            indexController.setRequest(rq);
            indexController.setResponse(rs);
            indexController.login();
            return null;
        });
        
        
        // GRUPOS
        get("/grupo", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            return grupoController.listar();
        }, new MustacheTemplateEngine());
        
        get("/grupo/", (rq, rs) -> {rs.redirect("/grupo"); return null;});
        
        get("/grupo/editar/:id", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            return grupoController.editar();
        }, new MustacheTemplateEngine());

        post("/grupo/salvar", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            grupoController.salvar();
            return null;
        });
        
        post("/grupo/adicionar/:grupo", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            grupoController.adicionar();
            return null;
        });
        
        post("/grupo/remover/:grupo", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            grupoController.remover();
            return null;
        });

        get("/grupo/excluir/:id", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            grupoController.excluir();
            return null;
        }, new MustacheTemplateEngine());
        
        
        
        // EQUIPES
        get("/equipe", (rq, rs) -> {
            equipeController.setRequest(rq);
            equipeController.setResponse(rs);
            return equipeController.listar();
        }, new MustacheTemplateEngine());
        
        get("/equipe/", (rq, rs) -> {rs.redirect("/equipe"); return null;});
        
        get("/equipe/editar/:id", (rq, rs) -> {
            int id = Integer.parseInt(rq.params(":id"));
            equipeController.setRequest(rq);
            equipeController.setResponse(rs);
            return equipeController.editar(id);
        }, new MustacheTemplateEngine());

        post("/equipe/salvar", (rq, rs) -> {
            equipeController.setRequest(rq);
            equipeController.setResponse(rs);
            equipeController.salvar();
            return null;
        });
        
        post("/equipe/adicionar/:equipe", (rq, rs) -> {
            equipeController.setRequest(rq);
            equipeController.setResponse(rs);
            equipeController.adicionar();
            return null;
        });
        
        post("/equipe/remover/:equipe", (rq, rs) -> {
            equipeController.setRequest(rq);
            equipeController.setResponse(rs);
            equipeController.remover();
            return null;
        });

        get("/equipe/excluir/:id", (rq, rs) -> {
            int id = Integer.parseInt(rq.params(":id"));
            equipeController.setRequest(rq);
            equipeController.setResponse(rs);
            equipeController.excluir(id);
            return null;
        }, new MustacheTemplateEngine());
        
        
        
        // JOGADORES
        get("/jogador", (rq, rs) -> {
            jogadorController.setRequest(rq);
            jogadorController.setResponse(rs);
            return jogadorController.listar();
        }, new MustacheTemplateEngine());
        
        get("/jogador/", (rq, rs) -> {rs.redirect("/jogador"); return null;});
        
        get("/jogador/editar/:id", (rq, rs) -> {
            int id = Integer.parseInt(rq.params(":id"));
            jogadorController.setRequest(rq);
            jogadorController.setResponse(rs);
            return jogadorController.editar(id);
        }, new MustacheTemplateEngine());

        post("/jogador/salvar", (rq, rs) -> {
            jogadorController.setRequest(rq);
            jogadorController.setResponse(rs);
            jogadorController.salvar();
            return null;
        });

        get("/jogador/excluir/:id", (rq, rs) -> {
            int id = Integer.parseInt(rq.params(":id"));
            jogadorController.setRequest(rq);
            jogadorController.setResponse(rs);
            jogadorController.excluir(id);
            return null;
        }, new MustacheTemplateEngine());
        
    }

}
