/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Persistence;
import modelo.Equipe;
import modelo.Grupo;
import modelo.Jogador;
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
            List<Equipe> e = dao.findEquipeEntities();
            map.put("equipes", e);
            map.put("vago", e.size() < 32);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        map.put("usuario", request.attribute("usuario"));
        map.put("logado", request.attribute("logado"));
        return new ModelAndView(map, "listar.equipe.mustache");
    }

    public ModelAndView editar(int id) {
        Map map = new HashMap();
        try {
            Equipe e = dao.findEquipe(id);
            map.put("equipe", e);
            if (id > 0) {
                boolean vago = e.getJogadores().size() < 23;
                if (vago){
                    map.put("jogadores",
                        new JogadorDAO(dao.getEntityManager().getEntityManagerFactory()).findPlayersNotInAnyTeam()
                    );
                }
                map.put("editando", true);
                map.put("vago", vago);
                map.put("temGrupo", e.getGrupo() != null);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        map.put("usuario", request.attribute("usuario"));
        map.put("logado", request.attribute("logado"));
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
            if (!dao.isRepeated(equipe)) {
                if (equipe.getId() > 0) {
                    Equipe e = dao.findEquipe(equipe.getId());
                    equipe.setJogadores(e.getJogadores());
                    equipe.setGrupo(e.getGrupo());
                    dao.edit(equipe);
                } else {
                    dao.create(equipe);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/equipe");
    }

    public void excluir(int id) {
        try {
            dao.destroy(id);
        } catch (NonexistentEntityException ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/equipe");
    }
    
    public void adicionar() {
        int equipeId = Integer.parseInt(request.params(":equipe"));
        try {
            int jogadorId = request.queryMap().get("jogador").integerValue();
            Equipe equipe = dao.findEquipe(equipeId);
            Jogador jogador = new JogadorDAO(dao.getEntityManager().getEntityManagerFactory()).findJogador(jogadorId);
            equipe.getJogadores().add(jogador);
            jogador.setEquipe(equipe);
            dao.edit(equipe);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/equipe/editar/" + equipeId);
    }
    
    public void remover() {
        int equipeId = Integer.parseInt(request.params(":equipe"));
        try {
            int jogadorId = request.queryMap().get("jogador").integerValue();
            Equipe equipe = dao.findEquipe(equipeId);
            Jogador jogador = new JogadorDAO(dao.getEntityManager().getEntityManagerFactory()).findJogador(jogadorId);
            equipe.getJogadores().remove(jogador);
            jogador.setEquipe(null);
            dao.edit(equipe);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/equipe/editar/" + equipeId);
    }
}
