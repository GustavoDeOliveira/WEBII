/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.Aluno;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

/**
 *
 * @author iapereira
 */
public class AlunoController {

    public ModelAndView listar(Request rq, Response rd) {
        Map map = new HashMap();
        ArrayList<Aluno> vetAluno = new ArrayList();
        vetAluno.add(new Aluno(11230193, "Gustavo"));
        vetAluno.add(new Aluno(43622, "Igor"));
        map.put("vetAluno", vetAluno);
//        map.put("vetAluno", new AlunoDAO().listar());
        return new ModelAndView(map, "listar.mustache");
    }

    public ModelAndView tela_adicionar(Request rq, Response rs) {      
        System.out.println("FEz o que tinha que fazer: tela_adicionar...");
        return new ModelAndView(new HashMap(), "tela_adicionar.mustache");    
    }
}
