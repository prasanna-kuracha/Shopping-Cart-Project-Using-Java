import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ButtonFrame extends JFrame {
    private DynamicButtonPanel buttonPanel;

    public ButtonFrame(Connection connection) {
        setTitle("Shopping_Cart");
        setLayout(new BorderLayout());

        // Create a main panel with black background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        buttonPanel = new DynamicButtonPanel(connection);
        // Ensure the buttonPanel has a transparent background
        buttonPanel.setOpaque(false);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Pack and center the frame on the screen
        pack();
        setLocationRelativeTo(null);
    }
}
