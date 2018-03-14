/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author aluno
 */
public class ConexaoPostgreSQL {
    private static String host, port, dbname, user, pass;
    
    public static Connection getConnection() {
        host = "localhost";
        port = "5432";
        dbname = "elifoot";
        user = "postgres";
        pass = "postgres";
        // jdbc:postgresql://localhost:5432/elifoot
        String url = "jdbc:postgresql://"+host+":"+port+"/"+dbname;
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("ERRO NA CONEXÃO:\n\n" + e.getMessage());
        }
    }
}
