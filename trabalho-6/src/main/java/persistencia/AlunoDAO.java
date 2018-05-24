package persistencia;

import javax.persistence.EntityManagerFactory;

public class AlunoDAO extends AlunoJPAController {
    
    public AlunoDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}
