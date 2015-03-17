/**
 * @(#)Agenda.java
 *
 * Agenda application
 *
 * @author Alex Wang
 * @version 1.00 2015/3/16
 */
 
import java.sql.*;
 
public class Agenda 
{
    
    public static void main(String[] args) 
    {
    	Connection c = null;
    	Statement stmt = null;
    	
    	try
    	{
    		Class.forName("org.sqlite.JDBC"); //loads reference to class object; ensures that it's loaded
    		c = DriverManager.getConnection("jdbc:sqlite:test.sqlite"); //creates/loads the db
    		System.out.println ("Opened database success");
    		
    		stmt = c.createStatement();
    		
    		//The command
    		String sql = "CREATE TABLE Categories " + //creates a table 'COMPANY'
    					 "(ID INT PRIMARY KEY    , " + //creates ID column
    					 " NAME			  TEXT   , " + //creates Name column
    					 " AGE			  INT    , " + //creates age column
    					 " Date        CHAR(50), " + //creates address column
    					 " SALARY		  REAL)"; //creates salary column
    					 
    					 
    		/*String sql = "INSERT INTO COMPANY(ID, NAME, AGE, ADDRESS, SALARY) " +
    					 "VALUES (1, 'Paul', 32, 'California', 2000);";*/
    		stmt.executeUpdate(sql); //updates/writes to the db
    		stmt.close();
    		c.close();
    	}
    	catch (Exception e)
    	{
    		System.err.println(e.getClass().getName() + ": " + e.getMessage());
    		System.exit(0);
    	}
    	System.out.println ("Table created");
    }
}
