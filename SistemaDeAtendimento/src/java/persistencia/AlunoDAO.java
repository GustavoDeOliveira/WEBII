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
import modelo.Aluno;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author aluno
 */
public class AlunoDAO implements Serializable {

    public AlunoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void criar(Aluno aluno) {
        if (aluno.getAtendimentos() == null) {
            aluno.setAtendimentos(new ArrayList<Atendimento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Atendimento> attachedAtendimentos = new ArrayList<Atendimento>();
            for (Atendimento atendimentosAtendimentoToAttach : aluno.getAtendimentos()) {
                atendimentosAtendimentoToAttach = em.getReference(atendimentosAtendimentoToAttach.getClass(), atendimentosAtendimentoToAttach.getId());
                attachedAtendimentos.add(atendimentosAtendimentoToAttach);
            }
            aluno.setAtendimentos(attachedAtendimentos);
            em.persist(aluno);
            for (Atendimento atendimentosAtendimento : aluno.getAtendimentos()) {
                Aluno oldAlunoOfAtendimentosAtendimento = atendimentosAtendimento.getAluno();
                atendimentosAtendimento.setAluno(aluno);
                atendimentosAtendimento = em.merge(atendimentosAtendimento);
                if (oldAlunoOfAtendimentosAtendimento != null) {
                    oldAlunoOfAtendimentosAtendimento.getAtendimentos().remove(atendimentosAtendimento);
                    oldAlunoOfAtendimentosAtendimento = em.merge(oldAlunoOfAtendimentosAtendimento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editar(Aluno aluno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aluno persistentAluno = em.find(Aluno.class, aluno.getId());
            List<Atendimento> atendimentosOld = persistentAluno.getAtendimentos();
            List<Atendimento> atendimentosNew = aluno.getAtendimentos();
            List<Atendimento> attachedAtendimentosNew = new ArrayList<Atendimento>();
            for (Atendimento atendimentosNewAtendimentoToAttach : atendimentosNew) {
                atendimentosNewAtendimentoToAttach = em.getReference(atendimentosNewAtendimentoToAttach.getClass(), atendimentosNewAtendimentoToAttach.getId());
                attachedAtendimentosNew.add(atendimentosNewAtendimentoToAttach);
            }
            atendimentosNew = attachedAtendimentosNew;
            aluno.setAtendimentos(atendimentosNew);
            aluno = em.merge(aluno);
            for (Atendimento atendimentosOldAtendimento : atendimentosOld) {
                if (!atendimentosNew.contains(atendimentosOldAtendimento)) {
                    atendimentosOldAtendimento.setAluno(null);
                    atendimentosOldAtendimento = em.merge(atendimentosOldAtendimento);
                }
            }
            for (Atendimento atendimentosNewAtendimento : atendimentosNew) {
                if (!atendimentosOld.contains(atendimentosNewAtendimento)) {
                    Aluno oldAlunoOfAtendimentosNewAtendimento = atendimentosNewAtendimento.getAluno();
                    atendimentosNewAtendimento.setAluno(aluno);
                    atendimentosNewAtendimento = em.merge(atendimentosNewAtendimento);
                    if (oldAlunoOfAtendimentosNewAtendimento != null && !oldAlunoOfAtendimentosNewAtendimento.equals(aluno)) {
                        oldAlunoOfAtendimentosNewAtendimento.getAtendimentos().remove(atendimentosNewAtendimento);
                        oldAlunoOfAtendimentosNewAtendimento = em.merge(oldAlunoOfAtendimentosNewAtendimento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aluno.getId();
                if (buscar(id) == null) {
                    throw new NonexistentEntityException("The aluno with id " + id + " no longer exists.");
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
            Aluno aluno;
            try {
                aluno = em.getReference(Aluno.class, id);
                aluno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aluno with id " + id + " no longer exists.", enfe);
            }
            List<Atendimento> atendimentos = aluno.getAtendimentos();
            for (Atendimento atendimentosAtendimento : atendimentos) {
                atendimentosAtendimento.setAluno(null);
                atendimentosAtendimento = em.merge(atendimentosAtendimento);
            }
            em.remove(aluno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aluno> listar() {
        return listar(true, -1, -1);
    }

    public List<Aluno> listar(int maxResults, int firstResult) {
        return listar(false, maxResults, firstResult);
    }

    private List<Aluno> listar(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aluno.class));
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

    public Aluno buscar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aluno.class, id);
        } finally {
            em.close();
        }
    }

    public int contar() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aluno> rt = cq.from(Aluno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
