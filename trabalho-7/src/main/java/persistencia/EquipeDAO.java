package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Equipe;

public class EquipeDAO extends EquipeJpaController {
    
    public EquipeDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
    public boolean isRepeated(Equipe e) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT e FROM Equipe e WHERE e.nome = :nome AND e.id != :id");
            q.setParameter("nome", e.getNome());
            q.setParameter("id", e.getId());
            return !q.getResultList().isEmpty();
        } finally {
            em.close();
        }
    }
    
    public List<Equipe> findTeamsNotInAnyGroup() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT e FROM Equipe e WHERE e.grupo IS NULL");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
