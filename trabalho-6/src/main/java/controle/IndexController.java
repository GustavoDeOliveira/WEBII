package controle;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import modelo.Usuario;
import persistencia.UsuarioDAO;
import sessao.UsuarioSessao;

@Controller
public class IndexController {

    @Inject
    private Result result;

    @Inject
    private Validator validation;

    @Inject
    private EntityManagerFactory emf;

    @Inject
    private UsuarioSessao sessao;

    private UsuarioDAO dao;

    public IndexController() {
        this.emf = Persistence.createEntityManagerFactory("default");
        this.dao = new UsuarioDAO(emf);
    }

    @Get
    @Path("/")
    public void index() {
        Usuario u = this.sessao == null ? null : sessao.getUsuario();
        if (u != null && u.temLoginESenha() && dao.findUsuario(u.getLogin(), u.getSenha()) != null) {
            result.redirectTo(AtividadeController.class).listar();
        } else {
            result.redirectTo(this).logar();
        }
    }

    @Get
    @Path("/logar/")
    public void logar() {
    }

    @Delete
    @Path("/deslogar/")
    public void deslogar() {
        sessao.setUsuario(null);
        result.redirectTo(this).logar();
    }

    @Post
    @Path("/logar/")
    public void logar(@NotNull @Valid Usuario usuario) {
        validation.onErrorForwardTo(this).logar();
        sessao.setUsuario(usuario);
        if ("admin".equals(usuario.getLogin()) && dao.findUsuario("admin", "admin") == null) {
            dao.create(usuario);
        }
        result.redirectTo(this).index();
    }
}
