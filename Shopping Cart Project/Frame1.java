import java.awt.Container;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
public class Frame1 extends JPanel{
    public Frame1()
    {

        
        try{
            String url="jdbc:mysql://localhost:3306/newdatabase";
            String user="root";   //root
            String pass="Mysql526@!#0" ;  //root password
        Connection con=DriverManager.getConnection(url,user,pass);
        if(con !=null)
        	// cREATE A STATEMENT OBJECT
        	
       System.out.println("Successfully Connected");
        Statement st=con.createStatement() ;
        
        ResultSet rs=st.executeQuery("select *from employee");
        
       
        while (rs.next()) {
            String item = rs.getString("name");
            displayArea.append(item + "\n");
        }

        add(displayArea);
		
        
        setSize(500,500);
        setTitle("Products Frame");
        
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
       // setResizable(false);
        }
            
        catch(SQLException e){
            System.out.println(e);
        }
        
        

    }
}
