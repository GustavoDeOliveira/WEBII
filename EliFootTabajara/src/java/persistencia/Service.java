package persistencia;

import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author gustavo
 * @param <TEntidade> A classe usada para o service
 */
public interface Service<TEntidade extends modelo.Entidade> {
    public void salvar(TEntidade e) throws SQLException;
    public void excluir(int id) throws SQLException;
    public void excluir(int[] ids) throws SQLException;
    public TEntidade carregar(int id) throws SQLException;
    public List<TEntidade> listar() throws SQLException;
}
