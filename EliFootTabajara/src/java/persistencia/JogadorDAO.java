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
    
    private Connection conn;

    
    @Override
    public boolean salvar(Jogador e) {
        return (e.persistente())?atualizar(e):inserir(e);
    }
    
    
    private boolean inserir(Jogador e) {
        if (e.getTime() == null){
            throw new RuntimeException("O Time do Jogador " + e.getNome() + " está nulo, jogadores precisam pertencer a um time");
        }
        String sql = "INSERT INTO Jogador (id, nome, time_id) VALUES (DEFAULT, ?, ?)";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                this.conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql, 1);
            query.setString(1, e.getNome());
            
            TimeDAO timeDAO = new TimeDAO();
            timeDAO.conn = conn;
            timeDAO.salvar(e.getTime());
            
            query.setInt(2, e.getTime().getId());
            query.executeUpdate();
            ResultSet rs = query.getGeneratedKeys();
            if(rs.next())
                e.setId(rs.getInt("id"));
            else
                throw new SQLException("Nenhuma chave foi gerada pelo insert.");
            
            if (connInterna)
                this.conn.close();
            
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO SALVAR JOGADOR " + e.getNome() + ":\n" + ex.getMessage());
        }
    }

    
    private boolean atualizar(Jogador e) {
        String sql = "UPDATE Jogador SET nome = ?, time_id = ? WHERE id = ?";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql);
            query.setString(1, e.getNome());
            
            TimeDAO timeDAO = new TimeDAO();
            timeDAO.conn = conn;
            timeDAO.salvar(e.getTime());
            
            query.setInt(2, e.getTime().getId());
            query.setInt(3, e.getId());
            query.executeUpdate();
            if (connInterna)
                conn.close();
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO ALTERAR JOGADOR " + e.getNome() + ":\n" + ex.getMessage());
        }
    }

    
    @Override
    public Jogador carregar(int id) {
        String sql = "SELECT * FROM Jogador WHERE id = ?";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql);
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();
            Jogador jogador = new Jogador("", new Time(""));
            TimeDAO timeDAO = new TimeDAO();
            timeDAO.conn = conn;
            if(rs.next()){
                jogador.setNome(rs.getString("nome"));
                jogador.setId(rs.getInt("id"));
                jogador.setTime(timeDAO.carregar(rs.getInt("time_id")));
            }
            if (connInterna)
                conn.close();
            return jogador;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO CARREGAR JOGADOR COM ID " + id + ":\n" + ex.getMessage());
        }
    }

    
    @Override
    public List<Jogador> listar() {
        String sql = "SELECT * FROM Jogador";
        
        try {
            boolean connInterna = this.conn == null || this.conn.isClosed();
            if (connInterna) {
                conn = ConexaoPostgreSQL.getConnection();
            }
            PreparedStatement query = conn.prepareStatement(sql);
            ResultSet rs = query.executeQuery();
            List<Jogador> jogadores = new ArrayList();
            TimeDAO timeDAO = new TimeDAO();
            timeDAO.conn = conn;
            while(rs.next()){
                Jogador jogador = new Jogador(
                        rs.getString("nome"), 
                        timeDAO.carregar(rs.getInt("time_id"))
                );
                jogador.setId(rs.getInt("id"));
                jogadores.add(jogador);
            }
            if (connInterna)
                conn.close();
            return jogadores;
        } catch (SQLException ex) {
            throw new RuntimeException("ERRO AO CARREGAR JOGADORES:\n" + ex.getMessage());
        }
    }

    
    @Override
    public boolean excluir(Jogador e) {
        String sql = "DELETE Jogador WHERE id = ?";
        
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
            throw new RuntimeException("ERRO AO EXCLUIR JOGADOR " + e.getNome() + ":\n" + ex.getMessage());
        }
    }
    
    
    @Override
    public boolean excluir(int[] ids) {
        StringBuilder sql = new StringBuilder("DELETE FROM jogador WHERE id IN (");
        
        for (int i = 0; i < ids.length - 1; i++) {
            sql.append("?");
            sql.append(", ");
        }
        sql.append("?)");
        
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
            throw new RuntimeException("ERRO AO EXCLUIR JOGADORES " + Arrays.toString(ids) + ":\n" + ex.getMessage());
        }
    }
}
