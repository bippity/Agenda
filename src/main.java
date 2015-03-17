/**
 * 
 * 
 * 
 */

import java.sql.*;
import java.util.*;

public class main 
{
	public static Database dbManager;
	private static Connection c;
	
	public static void main(String[] args) throws SQLException
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.sqlite");
			
			dbManager = new Database(c);
		} 
		catch (Exception e) 
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		
		Scanner scan = new Scanner(System.in);
		Boolean done = false;
		while (!done)
		{
			String in = scan.nextLine();
			if (!in.equals("exit"))
				//dbManager.InsertCategory(in);
				dbManager.InsertTab("hello", in);
			else
				done = true;
		}
	}

}
