package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Equipe;

public class JogadorDAO extends JogadorJpaController {
    
    public JogadorDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Equipe> findJogadoresNotInAnyEquipe() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT e FROM Equipe e WHERE e.grupo.id = 0");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
