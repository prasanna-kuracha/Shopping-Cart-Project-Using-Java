import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SecondaryWindow extends JFrame {
    private Connection connection; // Database connection
    private String tableName; // Table name

    public SecondaryWindow(String itemName, String tableName, Connection connection) {
        // Set the title of the window
        setTitle("Item Details: " + itemName);

        // Set the default close operation to dispose the window when closed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.connection = connection;
        this.tableName = tableName;

        try {
            // Prepare a SQL statement to select all columns from the specified table
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);

            // Execute the SQL statement and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Create a panel to hold the item details
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 2, 10, 10)); // Use GridLayout for side-by-side arrangement with 2 columns and 10px gaps

            // Iterate over the result set to display each item in a separate panel
            while (resultSet.next()) {
                // Retrieve the item name, description, price, and image URL from the result set
                String itemNameFromDB = resultSet.getString("List");
                int price = resultSet.getInt("Price");
                String imageURL = resultSet.getString("ImageURL"); // Assuming a column "ImageURL" contains the path to the image

                // Create a panel for the item
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BorderLayout()); // Use BorderLayout for simplicity
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Add a border for better visibility
                itemPanel.setPreferredSize(new Dimension(240, 350)); // Set preferred size

                // Load and resize the image
                ImageIcon imageIcon = new ImageIcon(imageURL);
                Image image = imageIcon.getImage();
                // Change dimensions here
                Image scaledImage = image.getScaledInstance(240, 240, Image.SCALE_SMOOTH); // Adjust dimensions as needed
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                itemPanel.add(imageLabel, BorderLayout.NORTH); // Add the image to the top of the panel

                // Create a panel for the text details
                JPanel textPanel = new JPanel();
                textPanel.setLayout(new GridLayout(2, 1)); // Use GridLayout for organized text arrangement

                // Create labels to display the item details
                JLabel nameLabel = new JLabel("Item Name: " + itemNameFromDB);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                textPanel.add(nameLabel); // Add the name label to the text panel

                JLabel priceLabel = new JLabel("Price: " + price);
                priceLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size
                priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                textPanel.add(priceLabel); // Add the price label to the text panel

                itemPanel.add(textPanel, BorderLayout.CENTER); // Add the text panel to the center of the item panel

                // Create a panel for buttons
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Use FlowLayout for side-by-side arrangement

                // Create "Buy" button
                JButton buyButton = createStyledButton("Buy");
                // Add action listener to the "Buy" button
                buyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int remainingQuantity = getRemainingQuantity(itemNameFromDB);
                            if (remainingQuantity <= 0) {
                                JOptionPane.showMessageDialog(SecondaryWindow.this, "The item is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                                return; // Exit the method
                            }

                            PreparedStatement updateStatement = connection.prepareStatement("UPDATE " + tableName + " SET Quantity = Quantity - 1 WHERE List = ?");
                            updateStatement.setString(1, itemNameFromDB);
                            int rowsUpdated = updateStatement.executeUpdate();
                            updateStatement.close();

                            JOptionPane.showMessageDialog(SecondaryWindow.this, "Order confirmed for item: " + itemNameFromDB, "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);

                            if (remainingQuantity == 1) { // If remaining quantity becomes 0
                                JOptionPane.showMessageDialog(SecondaryWindow.this, "The item is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                buttonPanel.add(buyButton); // Add the buy button to the button panel

                // Create "Add" button
                JButton addButton = createStyledButton("Add");
                // Add action listener to the "Add" button
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            PreparedStatement updateStatement = connection.prepareStatement("UPDATE " + tableName + " SET Quantity = Quantity + 1 WHERE List = ?");
                            updateStatement.setString(1, itemNameFromDB);
                            int rowsUpdated = updateStatement.executeUpdate();
                            updateStatement.close();

                            JOptionPane.showMessageDialog(SecondaryWindow.this, "Quantity added for item: " + itemNameFromDB, "Quantity Added", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                buttonPanel.add(addButton); // Add the add button to the button panel

                // Add the button panel to the bottom of the item panel
                itemPanel.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom

                // Add the item panel to the main panel
                panel.add(itemPanel);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();

            // Add a scroll pane to the main panel to make it scrollable
            JScrollPane scrollPane = new JScrollPane(panel);

            // Add the scroll pane to the frame
            add(scrollPane);

            // Set the size of the frame
            setSize(800, 600); // Set the width and height of the frame
            setResizable(true);

            // Center the frame on the screen
            setLocationRelativeTo(null);

            // Make the frame visible
            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Increased font size
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 179, 113)); // Custom background color (SeaGreen)
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Increased padding for larger size

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(46, 139, 87)); // Darker SeaGreen on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 179, 113)); // Original color when not hovered
            }
        });

        return button;
    }

    private int getRemainingQuantity(String itemNameFromDB) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT Quantity FROM " + tableName + " WHERE List = ?");
        statement.setString(1, itemNameFromDB);
        ResultSet resultSet = statement.executeQuery();
        int remainingQuantity = 0;
        if (resultSet.next()) {
            remainingQuantity = resultSet.getInt("Quantity");
        }
        resultSet.close();
        statement.close();
        return remainingQuantity;
    }

    public static void main(String[] args) {
        // Example usage (replace with actual database connection and table name)
        Connection connection = null; // Initialize your database connection
        String tableName = "your_table_name"; // Replace with your table name

        // Create and display the window
        SwingUtilities.invokeLater(() -> new SecondaryWindow("Example Item", tableName, connection));
    }
}
