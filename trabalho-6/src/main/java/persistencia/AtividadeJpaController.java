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
import modelo.Aluno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Atividade;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
public class AtividadeJpaController implements Serializable {

    public AtividadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Atividade atividade) {
        if (atividade.getAlunos() == null) {
            atividade.setAlunos(new ArrayList<Aluno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Aluno> attachedAlunos = new ArrayList<Aluno>();
            for (Aluno alunosAlunoToAttach : atividade.getAlunos()) {
                alunosAlunoToAttach = em.getReference(alunosAlunoToAttach.getClass(), alunosAlunoToAttach.getId());
                attachedAlunos.add(alunosAlunoToAttach);
            }
            atividade.setAlunos(attachedAlunos);
            em.persist(atividade);
            for (Aluno alunosAluno : atividade.getAlunos()) {
                alunosAluno.getAtividades().add(atividade);
                alunosAluno = em.merge(alunosAluno);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Atividade atividade) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atividade persistentAtividade = em.find(Atividade.class, atividade.getId());
            List<Aluno> alunosOld = persistentAtividade.getAlunos();
            List<Aluno> alunosNew = atividade.getAlunos();
            List<Aluno> attachedAlunosNew = new ArrayList<Aluno>();
            for (Aluno alunosNewAlunoToAttach : alunosNew) {
                alunosNewAlunoToAttach = em.getReference(alunosNewAlunoToAttach.getClass(), alunosNewAlunoToAttach.getId());
                attachedAlunosNew.add(alunosNewAlunoToAttach);
            }
            alunosNew = attachedAlunosNew;
            atividade.setAlunos(alunosNew);
            atividade = em.merge(atividade);
            for (Aluno alunosOldAluno : alunosOld) {
                if (!alunosNew.contains(alunosOldAluno)) {
                    alunosOldAluno.getAtividades().remove(atividade);
                    alunosOldAluno = em.merge(alunosOldAluno);
                }
            }
            for (Aluno alunosNewAluno : alunosNew) {
                if (!alunosOld.contains(alunosNewAluno)) {
                    alunosNewAluno.getAtividades().add(atividade);
                    alunosNewAluno = em.merge(alunosNewAluno);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = atividade.getId();
                if (findAtividade(id) == null) {
                    throw new NonexistentEntityException("The atividade with id " + id + " no longer exists.");
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
            Atividade atividade;
            try {
                atividade = em.getReference(Atividade.class, id);
                atividade.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atividade with id " + id + " no longer exists.", enfe);
            }
            List<Aluno> alunos = atividade.getAlunos();
            for (Aluno alunosAluno : alunos) {
                alunosAluno.getAtividades().remove(atividade);
                alunosAluno = em.merge(alunosAluno);
            }
            em.remove(atividade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Atividade> findAtividadeEntities() {
        return findAtividadeEntities(true, -1, -1);
    }

    public List<Atividade> findAtividadeEntities(int maxResults, int firstResult) {
        return findAtividadeEntities(false, maxResults, firstResult);
    }

    private List<Atividade> findAtividadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Atividade.class));
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

    public Atividade findAtividade(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Atividade.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtividadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Atividade> rt = cq.from(Atividade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
