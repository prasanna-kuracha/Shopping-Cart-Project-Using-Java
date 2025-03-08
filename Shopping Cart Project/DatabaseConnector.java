import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    
	static String url="jdbc:mysql://localhost:3306/newdatabase";
    static String user="root";   //root
   static  String pass="Mysql526@!#0" ; 
   
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
