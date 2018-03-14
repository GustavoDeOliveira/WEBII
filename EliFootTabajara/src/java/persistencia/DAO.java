package persistencia;

import java.util.List;

public interface DAO<TEntidade> {
    
    public boolean salvar(TEntidade e);
    public TEntidade carregar(int id);
    public List<TEntidade> listar();
    public boolean excluir(TEntidade e);
    public boolean excluir(int ids[]);
    
}
