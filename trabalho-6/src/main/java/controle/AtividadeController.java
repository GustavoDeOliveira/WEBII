package controle;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import modelo.Aluno;
import modelo.Atividade;
import persistencia.AlunoDAO;
import persistencia.AtividadeDAO;
import persistencia.exceptions.NonexistentEntityException;
import sessao.UsuarioSessao;

@Controller
public class AtividadeController {

    @Inject
    private Result result;

    @Inject
    private Validator validation;

    @Inject
    private EntityManagerFactory emf;
    
    @Inject
    private UsuarioSessao sessao;

    private AtividadeDAO dao;

    public AtividadeController() {
        this.emf = Persistence.createEntityManagerFactory("default");
        this.dao = new AtividadeDAO(emf);
    }

    @Get
    @Path("/atividade")
    public void listar() {
        result.include("usuario", sessao.getUsuario());
        this.result.include("atividades", dao.findAtividadeEntities());
    }

    @Get
    @Path("/atividade/{id}")
    public Atividade editar(int id) {
        result.include("usuario", sessao.getUsuario());
        if (id < 0) {
            return new Atividade();
        }
        result.include("alunos", new AlunoDAO(emf).findAlunoEntities());
        return dao.findAtividade(id);
    }

    @Get
    @Path("/atividade/excluir/{id}")
    public void excluir(int id) throws NonexistentEntityException {
        validation.onErrorForwardTo(this).listar();
        dao.destroy(id);
        this.result.redirectTo(this).listar();
    }

    @Post
    @Path("/atividade/alterar")
    public void editarPost(@NotNull @Valid Atividade atividade, List<Aluno> inscritos) throws Exception {
        validation.onErrorForwardTo(this).editar(atividade.getId());
        if (inscritos != null) {
            atividade.setAlunos(inscritos);
        } else {
            atividade.setAlunos(new ArrayList());
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
    
    @Get
    @Path("/atividade/adicionar/{id}")
    public void adicionarAluno(int id, int aluno) throws Exception {
        validation.onErrorForwardTo(this).editar(id);
        Atividade at = dao.findAtividade(id);
        Aluno al = new AlunoDAO(emf).findAluno(aluno);
        at.getAlunos().add(al);
        dao.edit(at);
        result.redirectTo(this).editar(id);
    }
    
    @Get
    @Path("/atividade/remover/{id}")
    public void removerAluno(int id, int aluno) throws Exception {
        validation.onErrorForwardTo(this).editar(id);
        Atividade at = dao.findAtividade(id);
        Aluno al = new AlunoDAO(emf).findAluno(aluno);
        at.getAlunos().remove(al);
        dao.edit(at);
        result.redirectTo(this).editar(id);
    }
}
