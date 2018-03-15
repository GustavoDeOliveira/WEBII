package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Jogador;
import modelo.Time;

public class TimeDAO implements DAO<Time> {
    @Override
    public void inserir(Time e, Connection conn) throws SQLException {
        String sql = "INSERT INTO \"time\" (id, nome) VALUES (DEFAULT, ?)";

        PreparedStatement query = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        query.setString(1, e.getNome());
        query.executeUpdate();
        ResultSet rs = query.getGeneratedKeys();
        rs.next();
        e.setId(rs.getInt("id"));
    }

    
    @Override
    public void atualizar(Time e, Connection conn) throws SQLException {
        String sql = "UPDATE \"time\" SET nome = ? WHERE id = ?";
        
        PreparedStatement query = conn.prepareStatement(sql);
        query.setString(1, e.getNome());
        query.setInt(2, e.getId());
        query.executeUpdate();
    }

    
    @Override
    public Time carregar(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM \"time\" WHERE id = ?";
        
        PreparedStatement query = conn.prepareStatement(sql);
        query.setInt(1, id);
        ResultSet rs = query.executeQuery();
        Time time = new Time("");
        if(rs.next()){
            time.setNome(rs.getString("nome"));
            time.setId(rs.getInt("id"));
            time = carregarJogadores(time, conn);
        }
        return time;
    }

    
    @Override
    public List<Time> listar(Connection conn) throws SQLException {
        String sql = "SELECT * FROM \"time\"";
        List<Time> times = new ArrayList();
        
        PreparedStatement query = conn.prepareStatement(sql);
        ResultSet rs = query.executeQuery();
        while(rs.next()){
            Time time = new Time(rs.getString("nome"));
            time.setId(rs.getInt("id"));
            times.add(carregarJogadores(time, conn));
        }
        return times;
    }
    
    
    private Time carregarJogadores(Time t, Connection conn) throws SQLException {
        String sql = "SELECT j.id, j.nome FROM Jogador j "
                + "INNER JOIN \"time\" t ON j.time_id = t.id "
                + "WHERE t.id = ?";
        
        PreparedStatement query = conn.prepareStatement(sql);
        query.setInt(1, t.getId());
        ResultSet rs = query.executeQuery();
        while(rs.next()){
            Jogador j = new Jogador(rs.getString("nome"), t);
            j.setId(rs.getInt("id"));
            t.addJogador(j);
        }
        return t;
    }

    
    @Override
    public void excluir(int id, Connection conn) throws SQLException {
        String sql = "DELETE FROM \"time\" WHERE id = ?";
        
        PreparedStatement query = conn.prepareStatement(sql);
        query.setInt(1, id);
        query.executeUpdate();
    }

    
    @Override
    public void excluir(int[] ids, Connection conn) throws SQLException {
        StringBuilder sql = new StringBuilder("DELETE FROM \"time\" WHERE id IN (");
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
