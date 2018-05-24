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
import modelo.Aluno;
import persistencia.AlunoDAO;
import persistencia.exceptions.NonexistentEntityException;

@Controller
public class AlunoController {

    @Inject
    private Result result;

    @Inject
    private Validator validation;

    @Inject
    private EntityManagerFactory emf;
    
    @Inject
    private AlunoDAO dao;

    public AlunoController() {
        this.emf = Persistence.createEntityManagerFactory("default");
    }

    @Path("/")
    public void index() {
        
        this.result.include("vetCliente", dao.findAlunoEntities());
    }
    
    
    @Get
    @Path("/cliente/tela_alterar/{id}")
    public Aluno editar(int id) {
        return dao.findAluno(id);        
    }    
    
    
    @Delete
    @Path("/cliente/excluir/{id}")
    public void excluir(int id) throws NonexistentEntityException {
        validation.onErrorForwardTo(this).index();
        dao.destroy(id);
        this.result.redirectTo(this).index();
    }
    
    
    @Put
    @Path("/cliente/alterar")
    public void editar(@NotNull @Valid Aluno aluno) throws Exception {
        validation.onErrorForwardTo(this).editar(aluno.getId());
        dao.edit(aluno);
        this.result.redirectTo(this).index();
    }

    @Post
    @Path("/cliente/adicionar")
    public void adicionar(@NotNull @Valid Aluno aluno) {
        validation.onErrorForwardTo(this).index();
        dao.create(aluno);
    }
}
