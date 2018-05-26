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
    
    private AlunoDAO dao;

    public AlunoController() {
        this.emf = Persistence.createEntityManagerFactory("default");
        this.dao = new AlunoDAO(emf);
    }

    @Get
    @Path("/aluno")
    public void listar() {
        this.result.include("alunos", dao.findAlunoEntities());
    }
    
    
    @Get
    @Path("/aluno/{id}")
    public Aluno editar(int id) {
        if (id < 0) return new Aluno();
        return dao.findAluno(id);        
    }    
    
    
    @Delete
    @Path("/aluno/{id}")
    public void excluir(int id) throws NonexistentEntityException {
        validation.onErrorForwardTo(this).listar();
        dao.destroy(id);
        this.result.redirectTo(this).listar();
    }
    
    
    @Put
    @Path("/aluno/editar")
    public void editarPost(@NotNull @Valid Aluno aluno) throws Exception {
        validation.onErrorForwardTo(this).editar(aluno.getId());
        dao.edit(aluno);
        this.result.redirectTo(this).listar();
    }

    @Post
    @Path("/aluno/")
    public void adicionar(@NotNull @Valid Aluno aluno) {
        validation.onErrorForwardTo(this).listar();
        dao.create(aluno);
    }
}
