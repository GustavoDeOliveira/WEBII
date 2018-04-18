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
import modelo.Atendimento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Professor;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author aluno
 */
public class ProfessorDAO implements Serializable {

    public ProfessorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void criar(Professor professor) {
        if (professor.getAtendimentos() == null) {
            professor.setAtendimentos(new ArrayList<Atendimento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Atendimento> attachedAtendimentos = new ArrayList<Atendimento>();
            for (Atendimento atendimentosAtendimentoToAttach : professor.getAtendimentos()) {
                atendimentosAtendimentoToAttach = em.getReference(atendimentosAtendimentoToAttach.getClass(), atendimentosAtendimentoToAttach.getId());
                attachedAtendimentos.add(atendimentosAtendimentoToAttach);
            }
            professor.setAtendimentos(attachedAtendimentos);
            em.persist(professor);
            for (Atendimento atendimentosAtendimento : professor.getAtendimentos()) {
                Professor oldProfessorOfAtendimentosAtendimento = atendimentosAtendimento.getProfessor();
                atendimentosAtendimento.setProfessor(professor);
                atendimentosAtendimento = em.merge(atendimentosAtendimento);
                if (oldProfessorOfAtendimentosAtendimento != null) {
                    oldProfessorOfAtendimentosAtendimento.getAtendimentos().remove(atendimentosAtendimento);
                    oldProfessorOfAtendimentosAtendimento = em.merge(oldProfessorOfAtendimentosAtendimento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editar(Professor professor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Professor persistentProfessor = em.find(Professor.class, professor.getId());
            List<Atendimento> atendimentosOld = persistentProfessor.getAtendimentos();
            List<Atendimento> atendimentosNew = professor.getAtendimentos();
            List<Atendimento> attachedAtendimentosNew = new ArrayList<Atendimento>();
            for (Atendimento atendimentosNewAtendimentoToAttach : atendimentosNew) {
                atendimentosNewAtendimentoToAttach = em.getReference(atendimentosNewAtendimentoToAttach.getClass(), atendimentosNewAtendimentoToAttach.getId());
                attachedAtendimentosNew.add(atendimentosNewAtendimentoToAttach);
            }
            atendimentosNew = attachedAtendimentosNew;
            professor.setAtendimentos(atendimentosNew);
            professor = em.merge(professor);
            for (Atendimento atendimentosOldAtendimento : atendimentosOld) {
                if (!atendimentosNew.contains(atendimentosOldAtendimento)) {
                    atendimentosOldAtendimento.setProfessor(null);
                    atendimentosOldAtendimento = em.merge(atendimentosOldAtendimento);
                }
            }
            for (Atendimento atendimentosNewAtendimento : atendimentosNew) {
                if (!atendimentosOld.contains(atendimentosNewAtendimento)) {
                    Professor oldProfessorOfAtendimentosNewAtendimento = atendimentosNewAtendimento.getProfessor();
                    atendimentosNewAtendimento.setProfessor(professor);
                    atendimentosNewAtendimento = em.merge(atendimentosNewAtendimento);
                    if (oldProfessorOfAtendimentosNewAtendimento != null && !oldProfessorOfAtendimentosNewAtendimento.equals(professor)) {
                        oldProfessorOfAtendimentosNewAtendimento.getAtendimentos().remove(atendimentosNewAtendimento);
                        oldProfessorOfAtendimentosNewAtendimento = em.merge(oldProfessorOfAtendimentosNewAtendimento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = professor.getId();
                if (buscar(id) == null) {
                    throw new NonexistentEntityException("The professor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void excluir(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Professor professor;
            try {
                professor = em.getReference(Professor.class, id);
                professor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The professor with id " + id + " no longer exists.", enfe);
            }
            List<Atendimento> atendimentos = professor.getAtendimentos();
            for (Atendimento atendimentosAtendimento : atendimentos) {
                atendimentosAtendimento.setProfessor(null);
                atendimentosAtendimento = em.merge(atendimentosAtendimento);
            }
            em.remove(professor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Professor> listar() {
        return listar(true, -1, -1);
    }

    public List<Professor> listar(int maxResults, int firstResult) {
        return listar(false, maxResults, firstResult);
    }

    private List<Professor> listar(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Professor.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            List result = q.getResultList();
            return result;
        } finally {
            em.close();
        }
    }

    public Professor buscar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Professor.class, id);
        } finally {
            em.close();
        }
    }

    public int contar() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Professor> rt = cq.from(Professor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
