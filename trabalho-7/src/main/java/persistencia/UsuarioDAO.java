package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Usuario;

public class UsuarioDAO extends UsuarioJpaController {

    public UsuarioDAO(EntityManagerFactory emf) {
        super(emf);
    }

    public Usuario findUsuario(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("findUsuarioByLoginAndPassword", Usuario.class);
            q.setParameter("login", login);
            q.setParameter("senha", senha);
            List result = q.getResultList();
            if (result.isEmpty()) return null;
            return (Usuario) result.get(0);
        } finally {
            em.close();
        }
    }

}
