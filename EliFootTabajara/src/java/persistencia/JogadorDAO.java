/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Jogador;
import modelo.Equipe;
import modelo.Posicao;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
public class JogadorDAO implements Serializable {

    public JogadorDAO(EntityManagerFactory emf) {
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
            Equipe time = jogador.getEquipe();
            if (time != null) {
                time = em.getReference(time.getClass(), time.getId());
                jogador.setEquipe(time);
            }
            Posicao posicao = jogador.getPosicao();
            if (posicao != null) {
                posicao = em.getReference(posicao.getClass(), posicao.getId());
                jogador.setPosicao(posicao);
            }
            em.persist(jogador);
            if (time != null) {
                time.getJogadores().add(jogador);
                time = em.merge(time);
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
            Equipe timeOld = persistentJogador.getEquipe();
            Equipe timeNew = jogador.getEquipe();
            if (timeNew != null) {
                timeNew = em.getReference(timeNew.getClass(), timeNew.getId());
                jogador.setEquipe(timeNew);
            }
            jogador = em.merge(jogador);
            if (timeOld != null && !timeOld.equals(timeNew)) {
                timeOld.getJogadores().remove(jogador);
                timeOld = em.merge(timeOld);
            }
            if (timeNew != null && !timeNew.equals(timeOld)) {
                timeNew.getJogadores().add(jogador);
                timeNew = em.merge(timeNew);
            }
            Posicao posicao = jogador.getPosicao();
            if (posicao != null) {
                posicao = em.getReference(posicao.getClass(), posicao.getId());
                jogador.setPosicao(posicao);
            }
            jogador = em.merge(jogador);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jogador.getId();
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
            Equipe time = jogador.getEquipe();
            if (time != null) {
                time.getJogadores().remove(jogador);
                time = em.merge(time);
            }
            em.remove(jogador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer[] ids) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Jogador> jogadores;
            try {
                jogadores = em.createNamedQuery("findJogadoresWithIds")
                        .setParameter("ids", ids)
                        .getResultList();
                jogadores.forEach((jogador) -> {
                    jogador.getId();
                });
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jogador with id " + ids + " no longer exists.", enfe);
            }
            final List<Equipe> times = new ArrayList();
            final EntityManager enMa = em;
            jogadores.forEach((jogador) -> {
                Equipe time = jogador.getEquipe();
                if (time != null) {
                    time.getJogadores().remove(jogador);
                    enMa.merge(times);
                }
            });
            em.remove(jogadores);
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

    public List<Jogador> findJogadorEntitiesById(Integer[] ids) {
        return findJogadorEntitiesById(ids, true, -1, -1);
    }

    public List<Jogador> findJogadorEntitiesById(Integer[] ids, int maxResults, int firstResult) {
        return findJogadorEntitiesById(ids, false, maxResults, firstResult);
    }

    private List<Jogador> findJogadorEntitiesById(Integer[] ids, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("findJogadoresWithIds");
            q.setParameter("ids", ids);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Jogador findJogador(Integer id) {
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
