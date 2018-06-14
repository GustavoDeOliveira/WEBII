/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Grupo;
import modelo.Jogador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Equipe;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
public class EquipeJpaController implements Serializable {

    public EquipeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Equipe equipe) {
        if (equipe.getJogadores() == null) {
            equipe.setJogadores(new ArrayList<Jogador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupo = equipe.getGrupo();
            if (grupo != null) {
                grupo = em.getReference(grupo.getClass(), grupo.getId());
                equipe.setGrupo(grupo);
            }
            List<Jogador> attachedJogadores = new ArrayList<Jogador>();
            for (Jogador jogadoresJogadorToAttach : equipe.getJogadores()) {
                jogadoresJogadorToAttach = em.getReference(jogadoresJogadorToAttach.getClass(), jogadoresJogadorToAttach.getId());
                attachedJogadores.add(jogadoresJogadorToAttach);
            }
            equipe.setJogadores(attachedJogadores);
            em.persist(equipe);
            if (grupo != null) {
                grupo.getEquipes().add(equipe);
                grupo = em.merge(grupo);
            }
            for (Jogador jogadoresJogador : equipe.getJogadores()) {
                Equipe oldEquipeOfJogadoresJogador = jogadoresJogador.getEquipe();
                jogadoresJogador.setEquipe(equipe);
                jogadoresJogador = em.merge(jogadoresJogador);
                if (oldEquipeOfJogadoresJogador != null) {
                    oldEquipeOfJogadoresJogador.getJogadores().remove(jogadoresJogador);
                    oldEquipeOfJogadoresJogador = em.merge(oldEquipeOfJogadoresJogador);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Equipe equipe) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipe persistentEquipe = em.find(Equipe.class, equipe.getId());
            Grupo grupoOld = persistentEquipe.getGrupo();
            Grupo grupoNew = equipe.getGrupo();
            List<Jogador> jogadoresOld = persistentEquipe.getJogadores();
            List<Jogador> jogadoresNew = equipe.getJogadores();
            List<String> illegalOrphanMessages = null;
            for (Jogador jogadoresOldJogador : jogadoresOld) {
                if (!jogadoresNew.contains(jogadoresOldJogador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jogador " + jogadoresOldJogador + " since its equipe field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grupoNew != null) {
                grupoNew = em.getReference(grupoNew.getClass(), grupoNew.getId());
                equipe.setGrupo(grupoNew);
            }
            List<Jogador> attachedJogadoresNew = new ArrayList<Jogador>();
            for (Jogador jogadoresNewJogadorToAttach : jogadoresNew) {
                jogadoresNewJogadorToAttach = em.getReference(jogadoresNewJogadorToAttach.getClass(), jogadoresNewJogadorToAttach.getId());
                attachedJogadoresNew.add(jogadoresNewJogadorToAttach);
            }
            jogadoresNew = attachedJogadoresNew;
            equipe.setJogadores(jogadoresNew);
            equipe = em.merge(equipe);
            if (grupoOld != null && !grupoOld.equals(grupoNew)) {
                grupoOld.getEquipes().remove(equipe);
                grupoOld = em.merge(grupoOld);
            }
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                grupoNew.getEquipes().add(equipe);
                grupoNew = em.merge(grupoNew);
            }
            for (Jogador jogadoresNewJogador : jogadoresNew) {
                if (!jogadoresOld.contains(jogadoresNewJogador)) {
                    Equipe oldEquipeOfJogadoresNewJogador = jogadoresNewJogador.getEquipe();
                    jogadoresNewJogador.setEquipe(equipe);
                    jogadoresNewJogador = em.merge(jogadoresNewJogador);
                    if (oldEquipeOfJogadoresNewJogador != null && !oldEquipeOfJogadoresNewJogador.equals(equipe)) {
                        oldEquipeOfJogadoresNewJogador.getJogadores().remove(jogadoresNewJogador);
                        oldEquipeOfJogadoresNewJogador = em.merge(oldEquipeOfJogadoresNewJogador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = equipe.getId();
                if (findEquipe(id) == null) {
                    throw new NonexistentEntityException("The equipe with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipe equipe;
            try {
                equipe = em.getReference(Equipe.class, id);
                equipe.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipe with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Jogador> jogadoresOrphanCheck = equipe.getJogadores();
            for (Jogador jogadoresOrphanCheckJogador : jogadoresOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipe (" + equipe + ") cannot be destroyed since the Jogador " + jogadoresOrphanCheckJogador + " in its jogadores field has a non-nullable equipe field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grupo grupo = equipe.getGrupo();
            if (grupo != null) {
                grupo.getEquipes().remove(equipe);
                grupo = em.merge(grupo);
            }
            em.remove(equipe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Equipe> findEquipeEntities() {
        return findEquipeEntities(true, -1, -1);
    }

    public List<Equipe> findEquipeEntities(int maxResults, int firstResult) {
        return findEquipeEntities(false, maxResults, firstResult);
    }

    private List<Equipe> findEquipeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Equipe.class));
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

    public Equipe findEquipe(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Equipe.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Equipe> rt = cq.from(Equipe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
