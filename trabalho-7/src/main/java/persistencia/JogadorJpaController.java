/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Equipe;
import modelo.Jogador;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
public class JogadorJpaController implements Serializable {

    public JogadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jogador jogador) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipe equipe = jogador.getEquipe();
            if (equipe != null) {
                equipe = em.getReference(equipe.getClass(), equipe.getId());
                jogador.setEquipe(equipe);
            }
            em.persist(jogador);
            if (equipe != null) {
                equipe.getJogadores().add(jogador);
                equipe = em.merge(equipe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jogador jogador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jogador persistentJogador = em.find(Jogador.class, jogador.getId());
            Equipe equipeOld = persistentJogador.getEquipe();
            Equipe equipeNew = jogador.getEquipe();
            if (equipeNew != null) {
                equipeNew = em.getReference(equipeNew.getClass(), equipeNew.getId());
                jogador.setEquipe(equipeNew);
            }
            jogador = em.merge(jogador);
            if (equipeOld != null && !equipeOld.equals(equipeNew)) {
                equipeOld.getJogadores().remove(jogador);
                equipeOld = em.merge(equipeOld);
            }
            if (equipeNew != null && !equipeNew.equals(equipeOld)) {
                equipeNew.getJogadores().add(jogador);
                equipeNew = em.merge(equipeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = jogador.getId();
                if (findJogador(id) == null) {
                    throw new NonexistentEntityException("The jogador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jogador jogador;
            try {
                jogador = em.getReference(Jogador.class, id);
                jogador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jogador with id " + id + " no longer exists.", enfe);
            }
            Equipe equipe = jogador.getEquipe();
            if (equipe != null) {
                equipe.getJogadores().remove(jogador);
                equipe = em.merge(equipe);
            }
            em.remove(jogador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jogador> findJogadorEntities() {
        return findJogadorEntities(true, -1, -1);
    }

    public List<Jogador> findJogadorEntities(int maxResults, int firstResult) {
        return findJogadorEntities(false, maxResults, firstResult);
    }

    private List<Jogador> findJogadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jogador.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Jogador findJogador(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jogador.class, id);
        } finally {
            em.close();
        }
    }

    public int getJogadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jogador> rt = cq.from(Jogador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
