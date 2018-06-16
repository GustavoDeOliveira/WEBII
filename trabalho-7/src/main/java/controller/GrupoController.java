/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Persistence;
import modelo.Grupo;
import persistencia.EquipeDAO;
import persistencia.GrupoDAO;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import spark.ModelAndView;

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
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao listar grupos.");
        }
        return new ModelAndView(map, "listar.grupo.mustache");
    }

    public ModelAndView editar(int id) {
        Map map = new HashMap();
        try {
            map.put("grupo", dao.findGrupo(id));
            if(id > 0)
            {
                map.put("equipes",
                    new EquipeDAO(dao.getEntityManager().getEntityManagerFactory()).findEquipesNotInAnyGroup()
                );
                map.put("editando", true);
            }
        } catch (Exception ex) {
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao editar grupo.");
        }
        return new ModelAndView(map, "editar.grupo.mustache");
    }
    
    public void salvar() {
        try {
            String idString = request.queryMap().get("id").value();
            int id = idString != null && !idString.isEmpty() ? Integer.parseInt(idString) : 0;
            String nome = request.queryMap().get("nome").value();
            String cidade = request.queryMap().get("cidade").value();
            Grupo grupo = new Grupo();
            grupo.setId(id);
            grupo.setNome(nome);
            grupo.setCidade(cidade);
            if (grupo.getId() > 0) {
                grupo.setEquipes(dao.findGrupo(grupo.getId()).getEquipes());
                dao.edit(grupo);
            } else {
                dao.create(grupo);
            }
        } catch (Exception ex) {
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao salvar grupo.");
        }
        response.redirect("/grupo");
    }
    
    public void excluir(int id) {
        try {
            dao.destroy(id);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao excluir grupo.");
        }
        response.redirect("/grupo");
    }
}
