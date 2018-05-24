package persistencia;

import javax.persistence.EntityManagerFactory;

public class AtividadeDAO extends AtividadeJPAController {
    
    public AtividadeDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}
