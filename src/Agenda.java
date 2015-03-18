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
//	public static Database dbManager;
//	private static Connection c;
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws SQLException
	{
		LoginFrame main = new LoginFrame();
	}
	
//	public static void initiateDB()
//	{
//		try
//		{
//			Class.forName("org.sqlite.JDBC");
//			c = DriverManager.getConnection("jdbc:sqlite:test.sqlite");
//			dbManager = new Database(c);
//			
//			Scanner scan = new Scanner(System.in);
//			Boolean done = false;
//			while (!done)
//			{
//				String in = scan.nextLine();
//				if (!in.equals("exit"))
//					//dbManager.InsertCategory(in);
//					dbManager.InsertTab("hello", in);
//				else
//					done = true;
//			}
//		}
//		catch (Exception e)
//		{
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//	}
}
