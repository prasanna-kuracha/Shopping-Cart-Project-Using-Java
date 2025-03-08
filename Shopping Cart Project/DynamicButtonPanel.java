import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicButtonPanel extends JPanel {
    private boolean buttonsAdded = false;
    private List<JButton> buttonList;
    private Connection connection; // Database connection
    private Map<String, String> tableMap; // Map to store button names and corresponding table names

    public DynamicButtonPanel(Connection connection) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Set layout to BoxLayout with Y_AXIS alignment
        buttonList = new ArrayList<>();
        tableMap = new HashMap<>(); // Initialize the table map

        JButton triggerButton = new JButton("PRODUCTS");
        triggerButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the main button to the center horizontally
        triggerButton.setPreferredSize(new Dimension(200, 60)); // Set preferred size
        triggerButton.setMaximumSize(new Dimension(155, 40)); // Set maximum size to prevent resizing
        triggerButton.setBackground(new Color(173, 216, 230)); // Set background color to light blue
        triggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!buttonsAdded) {
                    // Call method to add new buttons only if they haven't been added already
                    fetchAndDisplayButtons();
                    buttonsAdded = true;
                }
            }
        });
        add(Box.createVerticalStrut(20)); // Add vertical gap
        add(triggerButton); // Add the main button
        add(Box.createVerticalStrut(20)); // Add vertical gap

        // Initialize database connection
        this.connection = connection;

        // Populate the table map with button names and corresponding table names
        populateTableMap();
    }

    private void populateTableMap() {
        // Add button names and corresponding table names to the map
        tableMap.put("Electronics", "Electronics");
        tableMap.put("Cosmetics", "Cosmetics");
        tableMap.put("Clothes", "Clothes");
        tableMap.put("Groceries","Groceries");
        // Add more buttons and tables as needed
    }

    private void fetchAndDisplayButtons() {
        try {
            // Fetch items from the database
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ProductsTable");

            // Create buttons for each item and add them to the panel
            while (resultSet.next()) {
                String itemName = resultSet.getString("name");
                JButton button = new JButton(itemName);
                buttonList.add(button); // Add button to the list
                add(button); // Add button to the panel

                // Set preferred size for each button
                Dimension preferredSize = new Dimension(100, 30); // Adjust size as needed
                button.setPreferredSize(preferredSize);
                button.setMaximumSize(preferredSize); // Set maximum size to prevent resizing

                // Add vertical gap between buttons
                add(Box.createVerticalStrut(10)); // Adjust gap as needed

                // Add action listener to each button to open the secondary window
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openSecondaryWindow(itemName);
                    }
                });
            }

            resultSet.close();
            statement.close();

            // Update the layout to reflect changes
            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openSecondaryWindow(String itemName) {
        // Get the table name corresponding to the item
        String tableName = tableMap.get(itemName);
        if (tableName == null) {
            JOptionPane.showMessageDialog(this, "No table found for item: " + itemName);
            return;
        }

        // Open secondary window with details fetched from the specified table
        SecondaryWindow secondaryWindow = new SecondaryWindow(itemName, tableName, connection);
        secondaryWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        secondaryWindow.setVisible(true);
    }

    public List<JButton> getButtonList() {
        return buttonList;
    }
}
