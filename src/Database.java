/**
 * Database.java
 *
 */

import java.sql.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Database 
{
	private Connection c;
	private Statement db;
	
	public Database(Connection cn) throws SQLException
	{
		c = cn;
		
		db = c.createStatement();
		
		db.execute("create table if not exists Categories (ID INTEGER PRIMARY KEY, Name TEXT, TabID TEXT)");
		db.execute("create table if not exists Tabs (ID INTEGER PRIMARY KEY, Category TEXT, Info TEXT, Date STRING)");
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws SQLException 
	 */
	public void InsertCategory(String name) throws SQLException
	{
		
		String sql = MessageFormat.format("INSERT INTO Categories (Name) VALUES (\"{0}\");", name);
		//String sql = String.format("INSERT INTO Categories (Name) VALUES ('%s');", name);
		
		//System.out.println(sql);
		db.execute(sql);
	}
	
	/**
	 * 
	 * @param name
	 * @param text
	 * @return
	 * @throws SQLException 
	 */
	public void InsertTab(String cat, String text) throws SQLException
	{
		String timestamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm").format(Calendar.getInstance().getTime());
		String sql = MessageFormat.format("INSERT INTO Tabs (Category, Info, Date) VALUES (\"{0}\", \"{1}\", \"{2}\");", cat, text, timestamp);
		System.out.println(sql);	
		db.execute(sql);
		
		ResultSet rs= db.executeQuery("SELECT Max(ID) FROM Tabs");
		int id = rs.getInt(1); //why doesn't getInt("ID") work...
		
		String sql2 = MessageFormat.format("UPDATE Categories SET TabID=\"{0}\" WHERE Name=\"{1}\";", id, cat);
		db.executeUpdate(sql2);
	}
	
	
	/*public boolean ensureTable(String name) throws SQLException
	{
		return db.execute("create table if not exists " + name + "(ID INT, Name TEXT, TabID INT)");
	}*/
}
