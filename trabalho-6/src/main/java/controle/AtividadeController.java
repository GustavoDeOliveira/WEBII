package controle;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import modelo.Atividade;
import persistencia.AtividadeDAO;
import persistencia.exceptions.NonexistentEntityException;

@Controller
public class AtividadeController {

    @Inject
    private Result result;

    @Inject
    private Validator validation;

    @Inject
    private EntityManagerFactory emf;

    private AtividadeDAO dao;

    public AtividadeController() {
        this.emf = Persistence.createEntityManagerFactory("default");
        this.dao = new AtividadeDAO(emf);
    }

    @Get
    @Path("/atividade")
    public void listar() {
        this.result.include("atividades", dao.findAtividadeEntities());
    }

    @Get
    @Path("/atividade/{id}")
    public Atividade editar(int id) {
        if (id < 0) {
            return new Atividade();
        }
        return dao.findAtividade(id);
    }

    @Delete
    @Path("/atividade/{id}")
    public void excluir(int id) throws NonexistentEntityException {
        validation.onErrorForwardTo(this).listar();
        dao.destroy(id);
        this.result.redirectTo(this).listar();
    }

    @Post
    @Path("/atividade/alterar")
    public void editarPost(@NotNull @Valid Atividade atividade) throws Exception {
        if (atividade != null) {
            validation.onErrorForwardTo(this).editar(atividade.getId());
        } else {
            validation.onErrorForwardTo(this).listar();
        }
        dao.edit(atividade);
        this.result.redirectTo(this).listar();
    }

    @Post
    @Path("/atividade/")
    public void adicionar(@NotNull @Valid Atividade atividade) {
        validation.onErrorForwardTo(this).listar();
        dao.create(atividade);
        this.result.redirectTo(this).listar();
    }
}
