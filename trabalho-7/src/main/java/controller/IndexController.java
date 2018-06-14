/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.Jogador;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

/**
 *
 * @author iapereira
 */
public class IndexController extends Controller {

    public ModelAndView index() {
        Map map = new HashMap();
        ArrayList<Jogador> vetAluno = new ArrayList();
        //vetAluno.add(new Jogador(11230193, "Gustavo"));
        //vetAluno.add(new Jogador(43622, "Igor"));
        map.put("vetAluno", vetAluno);
//        map.put("vetAluno", new AlunoDAO().listar());
        return new ModelAndView(map, "listar.mustache");
    }

    public ModelAndView login() {      
        System.out.println("Logando usuário...");
        return new ModelAndView(new HashMap(), "tela_adicionar.mustache");    
    }
    
    public void logout() {
        System.out.println("Deslogando usuário...");
        index();
    }
}
