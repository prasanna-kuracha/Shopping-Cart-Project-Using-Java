import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen();

        // Connect to the database
        Connection connection = DatabaseConnector.getConnection(); // Assuming you have a method to obtain the database connection

        SwingUtilities.invokeLater(() -> {
            ButtonFrame frame = new ButtonFrame(connection); // Pass Connection object to ButtonFrame constructor
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 300);
            frame.setVisible(true);
            frame.setResizable(false);

            splashScreen.dispose(); // Close the splash screen
        });
    }
}
