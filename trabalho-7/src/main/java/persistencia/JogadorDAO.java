package persistencia;

import javax.persistence.EntityManagerFactory;

public class JogadorDAO extends JogadorJpaController {
    
    public JogadorDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}
