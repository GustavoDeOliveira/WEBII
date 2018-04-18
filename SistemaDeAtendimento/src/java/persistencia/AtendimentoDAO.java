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
import modelo.Aluno;
import modelo.Atendimento;
import modelo.Professor;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author aluno
 */
public class AtendimentoDAO implements Serializable {

    public AtendimentoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void criar(Atendimento atendimento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aluno aluno = atendimento.getAluno();
            if (aluno != null) {
                aluno = em.getReference(aluno.getClass(), aluno.getId());
                atendimento.setAluno(aluno);
            }
            Professor professor = atendimento.getProfessor();
            if (professor != null) {
                professor = em.getReference(professor.getClass(), professor.getId());
                atendimento.setProfessor(professor);
            }
            em.persist(atendimento);
            if (aluno != null) {
                aluno.getAtendimentos().add(atendimento);
                aluno = em.merge(aluno);
            }
            if (professor != null) {
                professor.getAtendimentos().add(atendimento);
                professor = em.merge(professor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editar(Atendimento atendimento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atendimento persistentAtendimento = em.find(Atendimento.class, atendimento.getId());
            Aluno alunoOld = persistentAtendimento.getAluno();
            Aluno alunoNew = atendimento.getAluno();
            Professor professorOld = persistentAtendimento.getProfessor();
            Professor professorNew = atendimento.getProfessor();
            if (alunoNew != null) {
                alunoNew = em.getReference(alunoNew.getClass(), alunoNew.getId());
                atendimento.setAluno(alunoNew);
            }
            if (professorNew != null) {
                professorNew = em.getReference(professorNew.getClass(), professorNew.getId());
                atendimento.setProfessor(professorNew);
            }
            atendimento = em.merge(atendimento);
            if (alunoOld != null && !alunoOld.equals(alunoNew)) {
                alunoOld.getAtendimentos().remove(atendimento);
                alunoOld = em.merge(alunoOld);
            }
            if (alunoNew != null && !alunoNew.equals(alunoOld)) {
                alunoNew.getAtendimentos().add(atendimento);
                alunoNew = em.merge(alunoNew);
            }
            if (professorOld != null && !professorOld.equals(professorNew)) {
                professorOld.getAtendimentos().remove(atendimento);
                professorOld = em.merge(professorOld);
            }
            if (professorNew != null && !professorNew.equals(professorOld)) {
                professorNew.getAtendimentos().add(atendimento);
                professorNew = em.merge(professorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = atendimento.getId();
                if (buscar(id) == null) {
                    throw new NonexistentEntityException("The atendimento with id " + id + " no longer exists.");
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
            Atendimento atendimento;
            try {
                atendimento = em.getReference(Atendimento.class, id);
                atendimento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atendimento with id " + id + " no longer exists.", enfe);
            }
            Aluno aluno = atendimento.getAluno();
            if (aluno != null) {
                aluno.getAtendimentos().remove(atendimento);
                aluno = em.merge(aluno);
            }
            Professor professor = atendimento.getProfessor();
            if (professor != null) {
                professor.getAtendimentos().remove(atendimento);
                professor = em.merge(professor);
            }
            em.remove(atendimento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Atendimento> listar() {
        return listar(true, -1, -1);
    }

    public List<Atendimento> listar(int maxResults, int firstResult) {
        return listar(false, maxResults, firstResult);
    }

    private List<Atendimento> listar(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Atendimento.class));
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

    public List<Atendimento> listarPorAluno(Integer alunoId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("listarAtendimentosPorAluno");
            q.setParameter("alunoId", alunoId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Atendimento> listarPorProfessor(Integer professorId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("listarAtendimentosPorProfessor");
            q.setParameter("professorId", professorId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Atendimento> listar(Integer[] ids) {
        if (ids == null) return new ArrayList();
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder("SELECT a FROM Atendimento a WHERE a.id IN (");
        for (int i = 0; i < ids.length - 1; i++) {
            sb.append(":id, ");
        }
        sb.append("id)");
        try {
            Query q = em.createQuery(sb.toString());
            for (int i = 0; i < ids.length; i++) {
                q.setParameter(i, ids[i]);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Atendimento buscar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Atendimento.class, id);
        } finally {
            em.close();
        }
    }

    public int contar() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Atendimento> rt = cq.from(Atendimento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
