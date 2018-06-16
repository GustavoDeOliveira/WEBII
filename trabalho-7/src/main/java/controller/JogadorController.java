/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Persistence;
import modelo.Jogador;
import modelo.Posicao;
import persistencia.JogadorDAO;
import persistencia.exceptions.NonexistentEntityException;
import spark.ModelAndView;

/**
 *
 * @author iapereira
 */
public class JogadorController extends Controller {

    private JogadorDAO dao;

    public JogadorController() {
        super();
        dao = new JogadorDAO(Persistence.createEntityManagerFactory("default"));
    }

    public ModelAndView listar() {
        Map map = new HashMap();
        try {
            map.put("jogadores", dao.findJogadorEntities());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        map.put("usuario", request.attribute("usuario"));
        map.put("logado", request.attribute("logado"));
        return new ModelAndView(map, "listar.jogador.mustache");
    }

    public ModelAndView editar(int id) {
        Map map = new HashMap();
        try {
            Jogador j = dao.findJogador(id);
            map.put("jogador", j);
            map.put("posicoes", Posicao.values());
            map.put("editando", id > 0);
            map.put("temEquipe", j.getEquipe() != null);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        map.put("usuario", request.attribute("usuario"));
        map.put("logado", request.attribute("logado"));
        return new ModelAndView(map, "editar.jogador.mustache");
    }

    public void salvar() {
        try {
            String idString = request.queryMap().get("id").value();
            int id = idString != null && !idString.isEmpty() ? Integer.parseInt(idString) : 0;
            int posicao = request.queryMap().get("posicao").integerValue();
            String nome = request.queryMap().get("nome").value();
            Jogador jogador = new Jogador();
            jogador.setId(id);
            jogador.setPosicao(Posicao.values()[posicao]);
            jogador.setNome(nome);
            if (jogador.getId() > 0) {
                Jogador e = dao.findJogador(jogador.getId());
                jogador.setEquipe(e.getEquipe());
                dao.edit(jogador);
            } else {
                dao.create(jogador);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/jogador");
    }

    public void excluir(int id) {
        try {
            dao.destroy(id);
        } catch (NonexistentEntityException ex) {
            System.err.println(ex.getMessage());
        }
        response.redirect("/jogador");
    }
}
