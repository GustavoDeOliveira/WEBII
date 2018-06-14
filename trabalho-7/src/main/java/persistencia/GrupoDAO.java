package persistencia;

import javax.persistence.EntityManagerFactory;

public class GrupoDAO extends GrupoJpaController {
    
    public GrupoDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}
