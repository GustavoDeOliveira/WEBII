/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Equipe;
import modelo.Jogador;
import modelo.Posicao;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gustavo
 */
public class EquipeDAOTest {
    
    EquipeDAO dao;
    
    public EquipeDAOTest() {
        dao = new EquipeDAO(Persistence.createEntityManagerFactory("default"));
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @org.junit.Test
    public void salvandoEquipeComUmJogador() {
        Jogador j = new Jogador();
        j.setIdade(20);
        j.setNome("Cleison");
        j.setPosicao(Posicao.GOLEIRO);
        Equipe e = new Equipe();
        e.setNome("Tabajara");
        e.getJogadores().add(j);
        dao.create(e);
        e = dao.findEquipeEntities().get(0);
        if (!e.getNome().equals("Tabajara")) {
            fail("Nome esperado: Tabajara, nome encontrado: " + e.getNome());
        }
        if (e.getJogadores().isEmpty()) {
            fail("O jogador não foi salvo.");
        }
        j = e.getJogadores().get(0);
        if (!j.getNome().equals("Cleison")) {
            fail("Nome esperado: Cleison, nome encontrado: " + j.getNome());
        }
    }
    
}
