package persistencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import modelo.Jogador;

/**
 *
 * @author gustavo
 */
public class JogadorService implements Service<Jogador>{

    private final DAO<Jogador> dao;

    public JogadorService() {
        this.dao = new JogadorDAO();
    }
    
    @Override
    public void salvar(Jogador e) throws RuntimeException {
        if (e.getNome() == null || e.getNome().isEmpty())
            throw new RuntimeException("O nome do jogador é obrigatório.");
        if (e.getTime() == null)
            throw new RuntimeException("O jogador  precisa estar em um time.");
        
        try(Connection conn = conn()) {
            if(e.persistente())
                dao.atualizar(e, conn);
            else
                dao.inserir(e, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro salvando jogador:\n" + ex.getMessage());
        }
    }

    @Override
    public void excluir(int id) throws RuntimeException {
        if(id == 0)
            throw new RuntimeException("Nenhum jogador para excluir.");
            
        try(Connection conn = conn()) {
            dao.excluir(id, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro excluindo jogador.");
        }
    }

    @Override
    public void excluir(int[] ids) throws RuntimeException {
        if(ids.length == 0)
            throw new RuntimeException("Nenhum jogador para excluir.");
        
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == 0)
                throw new RuntimeException("Um dos jogadores é inválido.");
        }
            
        try(Connection conn = conn()) {
            dao.excluir(ids, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro excluindo jogador.");
        }
    }

    @Override
    public Jogador carregar(int id) throws RuntimeException {
        if(id == 0)
            return new Jogador("", new modelo.Time(""));
            
        try(Connection conn = conn()) {
            return dao.carregar(id, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro carregando jogador.");
        }
    }

    @Override
    public List<Jogador> listar() throws RuntimeException {
        try(Connection conn = conn()) {
            return dao.listar(conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro listando jogadores:\n" + ex.getMessage());
        }
    }
    
    private Connection conn() throws SQLException {
        return ConexaoPostgreSQL.getConnection();
    }
}
