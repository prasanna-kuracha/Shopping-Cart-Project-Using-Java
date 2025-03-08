import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen() {
        JPanel content = (JPanel)getContentPane();
        content.setBackground(Color.black);

        int width = 1000;
        int height = 600;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        JLabel label = new JLabel(new ImageIcon("C:/Users/divya/OneDrive/Desktop/ShopCart/interface1.jpeg")); // Replace "splash_image.jpg" with your image path
        content.add(label, BorderLayout.CENTER);

        setVisible(true);

        try {
            Thread.sleep(2000); // Adjust the duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}