package controle;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import modelo.Cliente;
import persistencia.ClienteDAO;
import persistencia.exceptions.NonexistentEntityException;

@Controller
public class ClienteController {

    @Inject
    private Result result;

    @Inject
    private Validator validation;

    @Inject
    private EntityManagerFactory emf;

    public ClienteController() {
        this.emf = Persistence.createEntityManagerFactory("default");
    }

    @Path("/")
    public void index() {
        
        this.result.include("vetCliente", new ClienteDAO(emf).findClienteEntities());
    }
    
    
    @Get
    @Path("/cliente/tela_alterar/{id}")
    public Cliente tela_alterar(int id) {
        return new ClienteDAO(emf).findCliente(id);        
    }    
    
    
    @Get
    @Path("/cliente/excluir/{id}")
    public void excluir(int id) throws NonexistentEntityException {
        validation.onErrorForwardTo(this).index();
        new ClienteDAO(emf).destroy(id);
        this.result.redirectTo(this).index();
    }
    
    
    @Post
    @Path("/cliente/alterar")
    public void alterar(@NotNull @Valid Cliente cliente) throws Exception {
        validation.onErrorForwardTo(this).tela_alterar(cliente.getId());
        new ClienteDAO(emf).edit(cliente);
        this.result.redirectTo(this).index();
    }

    @Post
    @Path("/cliente/adicionar")
    public void adicionar(@NotNull @Valid Cliente cliente) {
        validation.onErrorForwardTo(this).index();
        new ClienteDAO(emf).create(cliente);
    }
}
