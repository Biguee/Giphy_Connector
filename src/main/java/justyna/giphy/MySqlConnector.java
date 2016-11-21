package justyna.giphy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Justyna Salacinska
 *
 */
public class MySqlConnector implements AutoCloseable{

    private final Connection connection;
    private final PreparedStatement likeStatement;
    private final PreparedStatement dislikeStatement;

    public MySqlConnector(String url, String user, String password, String table, String idColumn, String likeColumn, String dislikeColumn) throws SQLException{
        connection = DriverManager.getConnection(url, user, password);

        String likeQuery = "INSERT INTO " + table + "(" + idColumn + ", " + likeColumn + ", " + dislikeColumn + ") VALUES (?, 1, 0) ON DUPLICATE KEY UPDATE " + likeColumn + "=" + likeColumn + "+1;";
        likeStatement = connection.prepareStatement(likeQuery);

        String dislikeQuery = "INSERT INTO " + table + "(" + idColumn + ", " + dislikeColumn + ", " + likeColumn + ") VALUES (?, 1, 0) ON DUPLICATE KEY UPDATE " + dislikeColumn + "=" + dislikeColumn + "+1;";
        dislikeStatement = connection.prepareStatement(dislikeQuery);
    }

    public void increaseLikes(String id) {
        try {
            likeStatement.setString(1, id);
            likeStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void increaseDislikes(String id) {
        try {
            dislikeStatement.setString(1, id);
            dislikeStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void close() throws Exception {
        likeStatement.close();
        dislikeStatement.close();
        connection.close();
    }
}
