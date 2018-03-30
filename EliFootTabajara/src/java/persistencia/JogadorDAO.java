package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Jogador;
import modelo.Time;

public class JogadorDAO implements DAO<Jogador> {
    
    
    @Override
    public void inserir(Jogador e, Connection conn) throws SQLException {
        String sql = "INSERT INTO Jogador (id, nome, time_id) VALUES (DEFAULT, ?, ?)";
        
        PreparedStatement query = conn.prepareStatement(sql, 1);
        query.setString(1, e.getNome());

        new TimeService().salvar(e.getTime());

        query.setInt(2, e.getTime().getId());
        query.executeUpdate();
        ResultSet rs = query.getGeneratedKeys();
        rs.next();
        e.setId(rs.getInt("id"));
    }

    
    @Override
    public void atualizar(Jogador e, Connection conn) throws SQLException {
        String sql = "UPDATE Jogador SET nome = ?, time_id = ? WHERE id = ?";
        
        PreparedStatement query = conn.prepareStatement(sql);
        query.setString(1, e.getNome());

        new TimeService().salvar(e.getTime());

        query.setInt(2, e.getTime().getId());
        query.setInt(3, e.getId());
        query.executeUpdate();
    }

    
    @Override
    public Jogador carregar(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM Jogador WHERE id = ?";
        
        PreparedStatement query = conn.prepareStatement(sql);
        query.setInt(1, id);
        ResultSet rs = query.executeQuery();
        Jogador jogador = new Jogador("", new Time(""));
        TimeDAO timeDAO = new TimeDAO();
        if(rs.next()){
            jogador.setNome(rs.getString("nome"));
            jogador.setId(rs.getInt("id"));
            jogador.setTime(timeDAO.carregar(rs.getInt("time_id"), conn));
        }
        return jogador;
    }

    
    @Override
    public List<Jogador> listar(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Jogador";
        
        PreparedStatement query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        List<Jogador> jogadores = new ArrayList();
        TimeDAO timeDAO = new TimeDAO();
        while(rs.next()){
            Jogador jogador = new Jogador(
                rs.getString("nome"), 
                timeDAO.carregar(rs.getInt("time_id"), conn)
            );
            jogador.setId(rs.getInt("id"));
            jogadores.add(jogador);
        }
        return jogadores;
    }

    
    @Override
    public void excluir(int id, Connection conn) throws SQLException {
        String sql = "DELETE Jogador WHERE id = ?";
        
        PreparedStatement query = conn.prepareStatement(sql);
        query.setInt(1, id);
        query.executeUpdate();
    }
    
    
    @Override
    public void excluir(int[] ids, Connection conn) throws SQLException {
        StringBuilder sql = new StringBuilder("DELETE FROM jogador WHERE id IN (");
        
        for (int i = 0; i < ids.length - 1; i++) {
            sql.append("?, ");
        }
        sql.append("?)");
        
        PreparedStatement query = conn.prepareStatement(sql.toString());
        for (int i = 0; i < ids.length; i++) {
            query.setInt(i+1, ids[i]);
        }
        query.executeUpdate();
    }
}
