package persistencia;

import javax.persistence.EntityManagerFactory;

public class AlunoDAO extends AlunoJpaController {
    
    public AlunoDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}
