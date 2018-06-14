package persistencia;

import javax.persistence.EntityManagerFactory;

public class EquipeDAO extends EquipeJpaController {
    
    public EquipeDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}
