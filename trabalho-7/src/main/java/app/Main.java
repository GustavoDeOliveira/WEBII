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
import java.util.ArrayList;
import modelo.Jogador;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import spark.template.mustache.MustacheTemplateEngine;

public class Main {

    public static void main(String[] args) {

        staticFiles.location("/public"); // Static files
        
        AlunoController alunoController = new AlunoController();

//        before((Request request, Response response) -> {
//            System.out.println("=======================");
//            System.out.println("Veio antes..."+request.url());   
//            //System.out.println(request.queryMap().value());
//            System.out.println("=======================");
//            //halt(401, "sai daqui...."); 
//        });
//        
//        
//        after((request, response) -> {
//            System.out.println("=======================");
//            System.out.println("Veio depois...");            
//            System.out.println("=======================");
//        });

        get("/", (rq, rs) -> {
            alunoController.setRequest(rq);
            alunoController.setResponse(rs);
            return alunoController.tela_adicionar();
        }, new MustacheTemplateEngine());

        post("/adicionar", (req, res) -> {
            return "O que veio pra mim pelo form:" + req.queryMap().get("nome").value();
        });

        get("/deletar/:matricula", (req, res) ->  {
            return "O que veio pra mim pelo form:" + Integer.parseInt(req.params(":matricula"));
        });
        get("/listar", (rq, rs) -> {
            alunoController.setRequest(rq);
            alunoController.setResponse(rs);
            return alunoController.listar();
        }, new MustacheTemplateEngine());

    }

}
