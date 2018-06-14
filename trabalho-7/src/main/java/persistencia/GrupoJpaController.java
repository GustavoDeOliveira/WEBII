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
import modelo.Equipe;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Grupo;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
public class GrupoJpaController implements Serializable {

    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) {
        if (grupo.getEquipes() == null) {
            grupo.setEquipes(new ArrayList<Equipe>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Equipe> attachedEquipes = new ArrayList<Equipe>();
            for (Equipe equipesEquipeToAttach : grupo.getEquipes()) {
                equipesEquipeToAttach = em.getReference(equipesEquipeToAttach.getClass(), equipesEquipeToAttach.getId());
                attachedEquipes.add(equipesEquipeToAttach);
            }
            grupo.setEquipes(attachedEquipes);
            em.persist(grupo);
            for (Equipe equipesEquipe : grupo.getEquipes()) {
                Grupo oldGrupoOfEquipesEquipe = equipesEquipe.getGrupo();
                equipesEquipe.setGrupo(grupo);
                equipesEquipe = em.merge(equipesEquipe);
                if (oldGrupoOfEquipesEquipe != null) {
                    oldGrupoOfEquipesEquipe.getEquipes().remove(equipesEquipe);
                    oldGrupoOfEquipesEquipe = em.merge(oldGrupoOfEquipesEquipe);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getId());
            List<Equipe> equipesOld = persistentGrupo.getEquipes();
            List<Equipe> equipesNew = grupo.getEquipes();
            List<String> illegalOrphanMessages = null;
            for (Equipe equipesOldEquipe : equipesOld) {
                if (!equipesNew.contains(equipesOldEquipe)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Equipe " + equipesOldEquipe + " since its grupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Equipe> attachedEquipesNew = new ArrayList<Equipe>();
            for (Equipe equipesNewEquipeToAttach : equipesNew) {
                equipesNewEquipeToAttach = em.getReference(equipesNewEquipeToAttach.getClass(), equipesNewEquipeToAttach.getId());
                attachedEquipesNew.add(equipesNewEquipeToAttach);
            }
            equipesNew = attachedEquipesNew;
            grupo.setEquipes(equipesNew);
            grupo = em.merge(grupo);
            for (Equipe equipesNewEquipe : equipesNew) {
                if (!equipesOld.contains(equipesNewEquipe)) {
                    Grupo oldGrupoOfEquipesNewEquipe = equipesNewEquipe.getGrupo();
                    equipesNewEquipe.setGrupo(grupo);
                    equipesNewEquipe = em.merge(equipesNewEquipe);
                    if (oldGrupoOfEquipesNewEquipe != null && !oldGrupoOfEquipesNewEquipe.equals(grupo)) {
                        oldGrupoOfEquipesNewEquipe.getEquipes().remove(equipesNewEquipe);
                        oldGrupoOfEquipesNewEquipe = em.merge(oldGrupoOfEquipesNewEquipe);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = grupo.getId();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Equipe> equipesOrphanCheck = grupo.getEquipes();
            for (Equipe equipesOrphanCheckEquipe : equipesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Equipe " + equipesOrphanCheckEquipe + " in its equipes field has a non-nullable grupo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
