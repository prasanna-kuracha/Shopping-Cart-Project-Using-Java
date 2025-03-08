import java.awt.event.ActionListener;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
public class MainFrame extends JFrame implements ActionListener {
    JButton button1,button2;
    MainFrame(){
        setTitle("MainFrame");
        setSize(400,200);
        setLayout(null);
        button1=new JButton ("PRODUCTS");
        button1.setBounds(100,50,150,40);
        button1.addActionListener(this);        
        add(button1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);  
    }

    public void actionPerformed(ActionEvent e)
    {
               if(e.getSource()==button1) new Button1() ;
              
    	
    	

    }

    
}
