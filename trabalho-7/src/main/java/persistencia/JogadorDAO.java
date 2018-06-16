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
    
    public List<Equipe> findPlayersNotInAnyTeam() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT j FROM Jogador j WHERE j.equipe IS NULL");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
