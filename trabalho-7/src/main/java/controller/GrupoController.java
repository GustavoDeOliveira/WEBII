/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Persistence;
import modelo.Grupo;
import modelo.Jogador;
import persistencia.GrupoDAO;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

/**
 *
 * @author iapereira
 */
public class GrupoController extends Controller {

    private GrupoDAO dao;
    
    public GrupoController() {
        super();
        dao = new GrupoDAO(Persistence.createEntityManagerFactory("default"));
    }
    
    public ModelAndView listar() {
        Map map = new HashMap();
        try {
            map.put("grupos", dao.findGrupoEntities());
        } catch (Exception ex) {
            response.body(response.body() + "&msg=Erro ao listar grupos.");
        }
        return new ModelAndView(map, "listar.grupo.mustache");
    }

    public ModelAndView editar(int id) {
        Map map = new HashMap();
        try {
            map.put("grupo", dao.findGrupo(id));
        } catch (Exception ex) {
            response.body(response.body() + "&msg=Erro ao editar grupo.");
        }
        return new ModelAndView(map, "editar.grupo.mustache");
    }
    
    public ModelAndView salvar(Grupo grupo) {
        try {
            if (grupo.getId() > 0) {
                dao.edit(grupo);
            } else {
                dao.create(grupo);
            }
        } catch (Exception ex) {
            response.body(response.body() + "&msg=Erro ao salvar grupo.");
        }
        return listar();
    }
    
    public ModelAndView excluir(int id) {
        try {
            dao.destroy(id);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            response.body(response.body() + "&msg=Erro ao excluir grupo.");
        }
        return listar();
    }
}
