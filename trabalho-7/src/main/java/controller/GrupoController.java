/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Persistence;
import modelo.Equipe;
import modelo.Grupo;
import persistencia.EquipeDAO;
import persistencia.GrupoDAO;
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
            System.err.println(ex.getMessage());
        }
        map.put("usuario", request.attribute("usuario"));
        map.put("logado", request.attribute("logado"));
        return new ModelAndView(map, "listar.grupo.mustache");
    }

    public ModelAndView editar() {
        Map map = new HashMap();
        try {
            int id = Integer.parseInt(request.params(":id"));
            Grupo g = dao.findGrupo(id);
            map.put("grupo", g);
            if(id > 0)
            {
                boolean vago = g.getEquipes().size() < 4;
                if (vago){
                    map.put("equipes",
                        new EquipeDAO(dao.getEntityManager().getEntityManagerFactory()).findTeamsNotInAnyGroup()
                    );
                }
                map.put("vago", vago);
                map.put("editando", true);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        map.put("usuario", request.attribute("usuario"));
        map.put("logado", request.attribute("logado"));
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
            System.err.println(ex.getMessage());
        }
        response.redirect("/grupo");
    }
    
    public void excluir() {
        try {
            int id = Integer.parseInt(request.params(":id"));
            dao.destroy(id);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/grupo");
    }

    public void adicionar() {
        int grupoId = Integer.parseInt(request.params(":grupo"));
        try {
            int equipeId = request.queryMap().get("equipe").integerValue();
            Grupo grupo = dao.findGrupo(grupoId);
            Equipe equipe = new EquipeDAO(dao.getEntityManager().getEntityManagerFactory()).findEquipe(equipeId);
            grupo.getEquipes().add(equipe);
            equipe.setGrupo(grupo);
            dao.edit(grupo);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/grupo/editar/" + grupoId);
    }
    
    public void remover() {
        int grupoId = Integer.parseInt(request.params(":grupo"));
        try {
            int equipeId = request.queryMap().get("equipe").integerValue();
            Grupo grupo = dao.findGrupo(grupoId);
            Equipe equipe = new EquipeDAO(dao.getEntityManager().getEntityManagerFactory()).findEquipe(equipeId);
            grupo.getEquipes().remove(equipe);
            equipe.setGrupo(null);
            dao.edit(grupo);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/grupo/editar/" + grupoId);
    }
}
