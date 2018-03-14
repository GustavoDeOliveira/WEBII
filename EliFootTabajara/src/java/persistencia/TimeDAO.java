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
    
    Connection conn;

    
    @Override
    public boolean salvar(Time e) {
        return (e.persistente())?atualizar(e):inserir(e);
    }
    

    private boolean inserir(Time e) {
        String sql = "INSERT INTO \"time\" (id, nome) VALUES (DEFAULT, ?)";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            query.setString(1, e.getNome());
            query.executeUpdate();
            ResultSet rs = query.getGeneratedKeys();
            if(rs.next())
                e.setId(rs.getInt("id"));
            else
                throw new SQLException("Nenhuma chave foi gerada pelo insert.");
            if (connInterna)
                conn.close();
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO SALVAR TIME " + e.getNome() + ":\n" + ex.getMessage());
        }
    }

    
    private boolean atualizar(Time e) {
        String sql = "UPDATE \"time\" SET nome = ? WHERE id = ?";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql);
            query.setString(1, e.getNome());
            query.setInt(2, e.getId());
            query.executeUpdate();
            if (connInterna)
                conn.close();
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO ALTERAR TIME " + e.getNome() + ":\n" + ex.getMessage());
        }
    }

    
    @Override
    public Time carregar(int id) {
        String sql = "SELECT * FROM \"time\" WHERE id = ?";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql);
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();
            Time time = new Time("");
            if(rs.next()){
                time.setNome(rs.getString("nome"));
                time.setId(rs.getInt("id"));
                time = carregarJogadores(time, conn);
            }
            if (connInterna)
                conn.close();
            return time;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO CARREGAR TIME COM ID " + id + ":\n" + ex.getMessage());
        }
    }

    
    @Override
    public List<Time> listar() {
        String sql = "SELECT * FROM \"time\"";
        List<Time> times = new ArrayList();
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql);
            ResultSet rs = query.executeQuery();
            while(rs.next()){
                Time time = new Time(rs.getString("nome"));
                time.setId(rs.getInt("id"));
                times.add(carregarJogadores(time, conn));
            }
            if (connInterna)
                conn.close();
            return times;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO CARREGAR TIMES:\n" + ex.getMessage());
        }
    }
    
    
    private Time carregarJogadores(Time t, Connection conn) {
        String sql = "SELECT j.id, j.nome FROM Jogador j "
                + "INNER JOIN \"time\" t ON j.time_id = t.id "
                + "WHERE t.id = ?";
        
        try {
            PreparedStatement query = conn.prepareStatement(sql);
            query.setInt(1, t.getId());
            ResultSet rs = query.executeQuery();
            while(rs.next()){
                Jogador j = new Jogador(rs.getString("nome"), t);
                j.setId(rs.getInt("id"));
                t.addJogador(j);
            }
            return t;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO CARREGAR JOGADORES PARA O TIME " + t.getNome() + ":\n" + ex.getMessage());
        }
    }

    
    @Override
    public boolean excluir(Time e) {
        String sql = "DELETE FROM \"time\" WHERE id = ?";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql);
            query.setInt(1, e.getId());
            query.executeUpdate();
            if (connInterna)
                conn.close();
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO EXCLUIR TIME " + e.getNome() + ":\n" + ex.getMessage());
        }
    }

    
    @Override
    public boolean excluir(int[] ids) {
        StringBuilder sql = new StringBuilder("DELETE FROM \"time\" WHERE id IN (");
        
        for (int i = 0; i < ids.length; i++) {
            sql.append('?');
            if (i + 1 < ids.length) sql.append(", ");
            else sql.append(")");
        }
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql.toString());
            for (int i = 0; i < ids.length; i++) {
                query.setInt(i+1, ids[i]);
            }
            query.executeUpdate();
            if (connInterna)
                conn.close();
            return true;
        } catch (SQLException ex) {
        throw new RuntimeException("ERRO AO EXCLUIR TIMES " + Arrays.toString(ids) + ":\n" + ex.getMessage());
        }
    }
    
}
