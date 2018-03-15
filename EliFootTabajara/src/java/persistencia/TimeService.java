package persistencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import modelo.Time;

/**
 *
 * @author gustavo
 */
public class TimeService implements Service<Time>{

    private final DAO<Time> dao;

    public TimeService() {
        this.dao = new TimeDAO();
    }
    
    @Override
    public void salvar(Time e) throws RuntimeException {
        if (e.getNome() == null || e.getNome().isEmpty())
            throw new RuntimeException("O nome do time é obrigatório.");
        
        try(Connection conn = conn()) {
            if(e.persistente())
                dao.atualizar(e, conn);
            else
                dao.inserir(e, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro salvando time.");
        }
    }

    @Override
    public void excluir(int id) throws RuntimeException {
        if(id == 0)
            throw new RuntimeException("Nenhum time para excluir.");
            
        try(Connection conn = conn()) {
            dao.excluir(id, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro excluindo time.");
        }
    }

    @Override
    public void excluir(int[] ids) throws RuntimeException {
        if(ids.length == 0)
            throw new RuntimeException("Nenhum time para excluir.");
        
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == 0)
                throw new RuntimeException("Um dos times é inválido.");
        }
            
        try(Connection conn = conn()) {
            dao.excluir(ids, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro excluindo time.");
        }
    }

    @Override
    public Time carregar(int id) throws RuntimeException {
        if(id == 0)
            throw new RuntimeException("Nenhum time para carregar.");
            
        try(Connection conn = conn()) {
            return dao.carregar(id, conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro carregando time.");
        }
    }

    @Override
    public List<Time> listar() throws RuntimeException {
        try(Connection conn = conn()) {
            return dao.listar(conn);
        } catch(SQLException ex) {
            throw new RuntimeException("Erro listando times.");
        }
    }
    
    private Connection conn() throws SQLException {
        return ConexaoPostgreSQL.getConnection();
    }
}
