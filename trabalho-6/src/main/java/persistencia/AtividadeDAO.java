package persistencia;

import javax.persistence.EntityManagerFactory;

public class AtividadeDAO extends AtividadeJpaController {
    
    public AtividadeDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}
