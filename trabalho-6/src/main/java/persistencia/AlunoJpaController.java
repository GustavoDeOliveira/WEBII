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
import modelo.Atividade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Aluno;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
public class AlunoJpaController implements Serializable {

    public AlunoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aluno aluno) {
        if (aluno.getAtividades() == null) {
            aluno.setAtividades(new ArrayList<Atividade>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Atividade> attachedAtividades = new ArrayList<Atividade>();
            for (Atividade atividadesAtividadeToAttach : aluno.getAtividades()) {
                atividadesAtividadeToAttach = em.getReference(atividadesAtividadeToAttach.getClass(), atividadesAtividadeToAttach.getId());
                attachedAtividades.add(atividadesAtividadeToAttach);
            }
            aluno.setAtividades(attachedAtividades);
            em.persist(aluno);
            for (Atividade atividadesAtividade : aluno.getAtividades()) {
                atividadesAtividade.getAlunos().add(aluno);
                atividadesAtividade = em.merge(atividadesAtividade);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aluno aluno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aluno persistentAluno = em.find(Aluno.class, aluno.getId());
            List<Atividade> atividadesOld = persistentAluno.getAtividades();
            List<Atividade> atividadesNew = aluno.getAtividades();
            List<Atividade> attachedAtividadesNew = new ArrayList<Atividade>();
            for (Atividade atividadesNewAtividadeToAttach : atividadesNew) {
                atividadesNewAtividadeToAttach = em.getReference(atividadesNewAtividadeToAttach.getClass(), atividadesNewAtividadeToAttach.getId());
                attachedAtividadesNew.add(atividadesNewAtividadeToAttach);
            }
            atividadesNew = attachedAtividadesNew;
            aluno.setAtividades(atividadesNew);
            aluno = em.merge(aluno);
            for (Atividade atividadesOldAtividade : atividadesOld) {
                if (!atividadesNew.contains(atividadesOldAtividade)) {
                    atividadesOldAtividade.getAlunos().remove(aluno);
                    atividadesOldAtividade = em.merge(atividadesOldAtividade);
                }
            }
            for (Atividade atividadesNewAtividade : atividadesNew) {
                if (!atividadesOld.contains(atividadesNewAtividade)) {
                    atividadesNewAtividade.getAlunos().add(aluno);
                    atividadesNewAtividade = em.merge(atividadesNewAtividade);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = aluno.getId();
                if (findAluno(id) == null) {
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

    public void destroy(int id) throws NonexistentEntityException {
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
            List<Atividade> atividades = aluno.getAtividades();
            for (Atividade atividadesAtividade : atividades) {
                atividadesAtividade.getAlunos().remove(aluno);
                atividadesAtividade = em.merge(atividadesAtividade);
            }
            em.remove(aluno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aluno> findAlunoEntities() {
        return findAlunoEntities(true, -1, -1);
    }

    public List<Aluno> findAlunoEntities(int maxResults, int firstResult) {
        return findAlunoEntities(false, maxResults, firstResult);
    }

    private List<Aluno> findAlunoEntities(boolean all, int maxResults, int firstResult) {
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

    public Aluno findAluno(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aluno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlunoCount() {
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
