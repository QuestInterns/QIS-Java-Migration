package qis;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class DatabaseConnect {
    public static Connection con;
    static PreparedStatement command;
    static ResultSet result;
    
    public static void connect()
    {
        try
        {
            FileReader fr = new FileReader("C:\\Users\\Public\\IPadd.txt");
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            String IP = "";
            while(line != null)
            {
                IP = line;
                line = br.readLine();
            }
            dbase(IP);
            br.close();
        }
        catch(Exception ex)
        {
            //String newIP = JOptionPane.showInputDialog(null, "Cannot connect to the database! Enter new IP address:");
            config();
        }
    }
    
    public static void config()
    {
        String newIP = JOptionPane.showInputDialog(null, new JLabel("Cannot connect to the database! Enter new IP address:",JLabel.CENTER), "IP Address",
                JOptionPane.PLAIN_MESSAGE);
        
        if(newIP != null)
        {
            dbase(newIP);
        }
        else
        {
            System.exit(-1);
        }
    }
    
    public static void dbase(String newIP)
    {
        try
        {
            String databaseName = "dbqis";
            String driver = "com.mysql.jdbc.Driver";    //the mysql driver from the jar
            String url = "jdbc:mysql://" + newIP + "/" + databaseName; 
            String username = "root";   //database username
            String password = "";   //database password
            Class.forName(driver);  //so that is uses the driver
            
            con = DriverManager.getConnection(url,username,password);   //connects to the database
            
            try{
            File file = new File("C:\\Users\\Public\\IPadd.txt");
            file.getParentFile().mkdirs();
            PrintWriter pw = new PrintWriter(file);
            pw.println(newIP);
            pw.close();
            }
            catch(Exception ex){JOptionPane.showMessageDialog(null, ex); System.exit(-1);}
        }
        catch(Exception ex){
            config();
        }
    }
    
     public static boolean runQuery(String query)
    {
        try
        {
            command = con.prepareStatement(query);
            command.executeUpdate();
            return true;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error running the query! Info: \n" + ex.getMessage());
            return false;
        }
    }
    
    public static String getValue(String query, String col)
    {
        try
        {
            String value = "";
            command = con.prepareStatement(query);
            result = command.executeQuery();
            
            while(result.next())
            {
                value = result.getString(col);
            }
            return value;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error running the query! Info: \n" + ex.getMessage());
            return null;
        }
    }
}
