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
		
		checkTables();
	}
	
	
	public void dispose() throws SQLException
	{
		c.close();
		db.close();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws SQLException 
	 */
	public void InsertCategory(User user, String name) throws SQLException
	{
		
		String sql = MessageFormat.format("INSERT INTO Categories (Owner, Name) VALUES (\"{0}\", \"{1}\");", user.Name, name);
		//String sql = String.format("INSERT INTO Categories (Name) VALUES ('%s');", name);
		
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
	
	public void checkTables() throws SQLException
	{
		db.execute("create table if not exists Users (ID INTEGER PRIMARY KEY, Name STRING NOT NULL UNIQUE, Password TEXT, CatID STRING)");
		//creates Categories Table
		db.execute("create table if not exists Categories (ID INTEGER PRIMARY KEY, Owner TEXT, Name TEXT, TabID TEXT)");
		//creates Tabs Table
		db.execute("create table if not exists Tabs (ID INTEGER PRIMARY KEY, Owner TEXT, Category TEXT, Info TEXT, Date STRING)");

	}
	
	public void AddUser(User user)
	{
		//int ret = 0;
		try 
		{
			String sql = MessageFormat.format("INSERT INTO Users (Name, Password) VALUES (\"{0}\",\"{1}\");", user.Name,
					user.Pass);
			db.executeUpdate(sql);
			//ret = db.executeUpdate(sql);
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		/*if (1 > ret)
		{
			System.out.println("ALERT: User already exists!");
		}*/
	}
	
	public User getUserById(int id)
	{
		try 
		{
			return GetUser(new User(id, null, null, null));
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public User getUserByName(String name)
	{
		try
		{
			return GetUser(new User(0, name, null, null));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public User GetUser(User user)
	{
		String sql;
		
		if (0 != user.ID)
		{
			//System.out.println("Checking by ID");
			sql= MessageFormat.format("SELECT * FROM Users WHERE ID=\"{0}\";", user.ID);
		}
		else 
		{
			//System.out.println("Checking by name");
			sql = MessageFormat.format("SELECT * FROM Users WHERE Name=\"{0}\";", user.Name);
		}
		
		try 
		{
			ResultSet rs = db.executeQuery(sql);
			user = LoadUserFromResult(user, rs); //ResultSet is closed error
			
			return user;
		} 
		catch (Exception e) 
		{
			System.out.println("THIS ONE" + e.getMessage());
			return null;
		}
	}
	
	private User LoadUserFromResult(User user, ResultSet result) throws SQLException
	{
		user.ID = result.getInt("ID"); //might have to change to 1 instead of "ID"
		user.Pass = result.getString("Password"); //3
		user.catID = result.getString("CatID");
		
		return user;
	}
	/*public boolean ensureTable(String name) throws SQLException
	{
		return db.execute("create table if not exists " + name + "(ID INT, Name TEXT, TabID INT)");
	}*/
}