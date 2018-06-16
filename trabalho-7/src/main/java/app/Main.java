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
import controller.AlunoController;
import controller.EquipeController;
import controller.GrupoController;
import controller.JogadorController;
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
        
        AlunoController alunoController = new AlunoController();
        GrupoController grupoController = new GrupoController();
        EquipeController equipeController = new EquipeController();
        JogadorController jogadorController = new JogadorController();

        before((Request req, Response res) -> {
            if (req.session() == null || "".equals(req.session().attribute("usuario"))) {
                
            }
        });
//        
//        
//        after((request, response) -> {
//            System.out.println("=======================");
//            System.out.println("Veio depois...");            
//            System.out.println("=======================");
//        });

//        get("/", (rq, rs) -> {
//            alunoController.setRequest(rq);
//            alunoController.setResponse(rs);
//            return alunoController.tela_adicionar();
//        }, new MustacheTemplateEngine());
//
//        post("/adicionar", (req, res) -> {
//            return "O que veio pra mim pelo form:" + req.queryMap().get("nome").value();
//        });
//
//        get("/deletar/:matricula", (req, res) ->  {
//            return "O que veio pra mim pelo form:" + Integer.parseInt(req.params(":matricula"));
//        });
//        get("/listar", (rq, rs) -> {
//            alunoController.setRequest(rq);
//            alunoController.setResponse(rs);
//            return alunoController.listar();
//        }, new MustacheTemplateEngine());
        
        // GRUPOS

        get("/grupo", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            return grupoController.listar();
        }, new MustacheTemplateEngine());
        
        get("/grupo/", (rq, rs) -> {rs.redirect("/grupo"); return null;});
        
        get("/grupo/editar/:id", (rq, rs) -> {
            int id = Integer.parseInt(rq.params(":id"));
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            return grupoController.editar(id);
        }, new MustacheTemplateEngine());

        post("/grupo/salvar", (rq, rs) -> {
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            grupoController.salvar();
            return null;
        });

        get("/grupo/excluir/:id", (rq, rs) -> {
            int id = Integer.parseInt(rq.params(":id"));
            grupoController.setRequest(rq);
            grupoController.setResponse(rs);
            grupoController.excluir(id);
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
