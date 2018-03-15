package persistencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DAO<TEntidade> {
    
    public void inserir(TEntidade e, Connection conn) throws SQLException;
    public void atualizar(TEntidade e, Connection conn) throws SQLException;
    
    public void excluir(int id, Connection conn) throws SQLException;
    public void excluir(int ids[], Connection conn) throws SQLException;
    
    public TEntidade carregar(int id, Connection conn) throws SQLException;
    public List<TEntidade> listar(Connection conn) throws SQLException;
    
}
