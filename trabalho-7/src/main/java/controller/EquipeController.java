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
import persistencia.EquipeDAO;
import persistencia.JogadorDAO;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import spark.ModelAndView;

/**
 *
 * @author iapereira
 */
public class EquipeController extends Controller {

    private EquipeDAO dao;

    public EquipeController() {
        super();
        dao = new EquipeDAO(Persistence.createEntityManagerFactory("default"));
    }

    public ModelAndView listar() {
        Map map = new HashMap();
        try {
            map.put("equipes", dao.findEquipeEntities());
        } catch (Exception ex) {
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao listar equipes.");
        }
        return new ModelAndView(map, "listar.equipe.mustache");
    }

    public ModelAndView editar(int id) {
        Map map = new HashMap();
        try {
            map.put("equipe", dao.findEquipe(id));
            if (id > 0) {
                map.put("jogadores",
                        new JogadorDAO(dao.getEntityManager().getEntityManagerFactory()).findJogadoresNotInAnyEquipe()
                );
                map.put("editando", true);
            }
        } catch (Exception ex) {
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao editar equipe.");
        }
        return new ModelAndView(map, "editar.equipe.mustache");
    }

    public void salvar() {
        try {
            String idString = request.queryMap().get("id").value();
            int id = idString != null && !idString.isEmpty() ? Integer.parseInt(idString) : 0;
            String nome = request.queryMap().get("nome").value();
            Equipe equipe = new Equipe();
            equipe.setId(id);
            equipe.setNome(nome);
            if (equipe.getId() > 0) {
                Equipe e = dao.findEquipe(equipe.getId());
                equipe.setJogadores(e.getJogadores());
                equipe.setGrupo(e.getGrupo());
                dao.edit(equipe);
            } else {
                dao.create(equipe);
            }
        } catch (Exception ex) {
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao salvar equipe.");
        }
        response.redirect("/equipe");
    }

    public void excluir(int id) {
        try {
            dao.destroy(id);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            System.err.println(ex);
            response.body(response.body() + "&msg=Erro ao excluir equipe.");
        }
        response.redirect("/equipe");
    }
}
