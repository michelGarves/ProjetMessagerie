/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.noswing.messagerie;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author thbogusz
 */
public class JdbcMessage {
    private Connection conn;
    public JdbcMessage() {
        try {
            // create our mysql database connection
            String myDriver = "com.mysql.cj.jdbc.Driver";
            Class.forName(myDriver);

            // déclaration URL base de données... règlage du TIMESTAMP
            String myUrl = "jdbc:mysql://localhost/bdmessage"
                    + "?useUnicode=true&useJDBCCompliantTimezoneShift=true"
                    + "&useLegacyDatetimeCode=false&serverTimezone=UTC";

            //Connection à la base bdmessage sous MYSQL
            conn = DriverManager.getConnection(myUrl, "root", "");

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    public ResultSet getAllMessages(String identifiant) {
        String query = "SELECT m.id, m.origineUsers, m.destinataireUsers, m.objet, m.message, m.dateEnvoi, m.etat "
                + "FROM message m INNER JOIN users u "
                + "ON u.id = m.origineUsers OR "
                + "u.identifiant = m.destinataireUsers "
                + "WHERE m.origineUsers = (SELECT id FROM users WHERE identifiant = '" + identifiant + "')"
                + "OR m.destinataireUsers = (SELECT id FROM users WHERE identifiant ='" + identifiant + "')";
        ResultSet rs = null;
        try {
            // create the java statement
            Statement st = conn.createStatement();
            
            // execute the query, and get a java resultset
            
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            System.err.print(e);
        }

        return rs;
    }
    public void insertMessage(String[] données){

        String sql = "INSERT into message "
                + "(origineUsers,"
                + "destinataireUsers,"
                + "objet,"
                + "message)"
                + " VALUES("
                    + "(SELECT id FROM users WHERE identifiant = '" + données[0] + "'),"
                    + "(SELECT id FROM users WHERE identifiant = '" + données[1] + "'),"
                    + "?,?)";
        try {
            PreparedStatement prepare;
            prepare = conn.prepareStatement(sql);
            prepare.setString(1, données[2]);
            prepare.setString(2, données[3]);

            int r = prepare.executeUpdate();
            prepare.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    public void deleteMessage(int id) {
    String sql = "DELETE FROM message"
                + " where id=?";

        try {
            conn.setAutoCommit(false);

            PreparedStatement prepare = conn.prepareStatement(sql);
           
            prepare.setInt(1, id);

            int r = prepare.executeUpdate();
            conn.commit();
            

        } catch (SQLException e) {
            System.err.print(e);
            
        }
    }
    
    
}
