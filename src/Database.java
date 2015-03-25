/**
 * Database.java
 * @author Alex Wang
 */

import java.sql.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database 
{
	private Connection c;
	private Statement db;
	
	/**
	 * Constructor
	 * @param cn Connection to database
	 * @throws SQLException
	 */
	public Database(Connection cn) throws SQLException
	{
		c = cn;
		
		db = c.createStatement();
		
		checkTables();
	}
	
	/**
	 * Dispose method
	 * @throws SQLException
	 */
	public void dispose() throws SQLException
	{
		c.close();
		db.close();
	}
	
	/**
	 * Creates sql tables if they do not exist
	 * @throws SQLException
	 */
	public void checkTables() throws SQLException
	{
		//Creates Users Table
		db.execute("create table if not exists Users (ID INTEGER PRIMARY KEY, Name STRING NOT NULL UNIQUE, Password TEXT)");
		
		//creates Categories Table
		db.execute("create table if not exists Categories (ID INTEGER PRIMARY KEY, Owner TEXT, Name STRING)");

		//creates Tabs Table
		db.execute("create table if not exists Tabs (ID INTEGER PRIMARY KEY, Name STRING, CatID INTEGER, Info TEXT, Date STRING)");
	}
	
	/**
	 * Adds a user to the database
	 * @param user User that is being registered into the database
	 */
	public void AddUser(User user)
	{
		try 
		{
			String sql = MessageFormat.format("INSERT INTO Users (Name, Password) VALUES (\"{0}\",\"{1}\");", user.Name,
					user.Pass);
			db.executeUpdate(sql);
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Returns a user by name if found in the database.
	 * @param name The user's name
	 * @return A user if found, or else null.
	 */
	public User getUserByName(String name)
	{
		try
		{
			return GetUser(new User(0, name, null));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns a user if found in the database
	 * @param user The user to be retrieved from the database
	 * @return A user if found, or else null
	 */
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
		catch (Exception e) //user doesn't exist
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Fully constructs the user that was given
	 * @param user Given user to be fully constructed
	 * @param result ResultSet from a database
	 * @return Constructed user
	 * @throws SQLException
	 */
	private User LoadUserFromResult(User user, ResultSet result) throws SQLException
	{
		user.ID = result.getInt("ID");
		user.Pass = result.getString("Password");
		
		return user;
	}
	
	/**
	 * Adds a new Category to the database
	 * @param user The owner of the category
	 * @param cat The Category that is to be added
	 */
	public void InsertCategory(User user, Category cat)
	{
		try
		{
			String sql = MessageFormat.format("INSERT INTO Categories (Owner, Name) VALUES (\"{0}\", \"{1}\");", user.Name, cat.Name);
			db.executeUpdate(sql);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Deletes a category from the database
	 * @param cat The category to be deleted
	 * @throws SQLException
	 */
	public void deleteCategory(Category cat) throws SQLException
	{
		String sql = MessageFormat.format("DELETE FROM Categories WHERE ID=\"{0}\";", cat.ID);
		db.executeUpdate(sql);
		
		String sql2 = MessageFormat.format("DELETE FROM Tabs WHERE CatID=\"{0}\";", cat.ID);
		db.executeUpdate(sql2);
	}
	
	/**
	 * Returns a Category by name if found in the database
	 * @param name The name of the Category
	 * @param user The owner of the Category
	 * @return A category if found in the database, else it is null
	 */
	public Category getCategoryByName(String name, User user)
	{
		try
		{
			return getCategory(new Category(0, user.Name, name));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns a Category if found in the database.
	 * @param cat The Category to be retrieved from the database
	 * @return A category that matches the given Category, else returns null
	 */
	public Category getCategory(Category cat)
	{
		String sql;
		
		if (0 != cat.ID)
		{
			//System.out.println("Checking by ID");
			sql= MessageFormat.format("SELECT * FROM Categories WHERE ID=\"{0}\";", cat.ID);
		}
		else 
		{
			sql = MessageFormat.format("SELECT * FROM Categories WHERE Owner=\"{0}\" AND Name=\"{1}\";",cat.Owner, cat.Name);
		}
		
		try 
		{
			ResultSet rs = db.executeQuery(sql);
			System.out.println (rs.getString("Name"));
			cat = LoadCategoryFromResult(cat, rs);
			
			return cat;
		} 
		catch (Exception e) //category doesn't exist
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns an ArrayList of Category owned by an user
	 * @param user The owner to be matched with
	 * @return ArrayList of Category that belongs to user
	 */
	public ArrayList<Category> getCategories(User user)
	{
		ArrayList<Category> list = new ArrayList<Category>();
		try
		{
			String sql = MessageFormat.format("SELECT * FROM Categories WHERE Owner=\"{0}\";", user.Name);
			ResultSet rs = db.executeQuery(sql);
			
			while (rs.next())
			{
				int id = rs.getInt("ID");
				String owner = rs.getString("Owner");
				String name = rs.getString("Name");
				
				list.add(new Category(id, owner, name));
			}
			rs.close();
		}
		catch (Exception e)
		{
			System.out.println (e.getMessage());
		}
		return list;
	}
	
	/**
	 * Fully constructs a category 
	 * @param cat The given category to be constructed
	 * @param result ResultSet from database
	 * @return Fully constructed cat
	 * @throws SQLException
	 */
	private Category LoadCategoryFromResult(Category cat, ResultSet result) throws SQLException
	{
		cat.ID = result.getInt("ID");
		cat.Owner = result.getString("Owner");
		cat.Name = result.getString("Name");
		
		return cat;
	}
	
	
	/**
	 * Inserts a new Tab into the database
	 * @param name Name of the Tab
	 * @param catID ID of Category the tab belongs to
	 */
	public void InsertTab(String name, int catID)
	{
		try
		{
			String timestamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm").format(Calendar.getInstance().getTime());
			String sql = MessageFormat.format("INSERT INTO Tabs (Name, CatID, Date) VALUES (\"{0}\", \"{1}\", \"{2}\");", name, catID, timestamp);
	
			db.execute(sql);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Deletes a Tab from the database
	 * @param tab The Tab to be deleted
	 * @throws SQLException
	 */
	public void deleteTab(Tab tab) throws SQLException
	{
		String sql = MessageFormat.format("DELETE FROM Tabs WHERE ID=\"{0}\";", tab.ID);
		db.executeUpdate(sql);
	}
	
	/**
	 * Returns a Tab by name if found in the database
	 * @param name Name of Tab to be found
	 * @param catID ID of category that the Tab belongs to
	 * @return Tab that matches the given parameters, else null
	 */
	public Tab getTabByName(String name, int catID)
	{
		try
		{
			return getTab(new Tab(0, name, catID, null, null));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns a Tab from the database
	 * @param tab Tab to be found
	 * @return Tab that matches the given Tab, else null
	 */
	public Tab getTab(Tab tab)
	{
		String sql;
		
		if (0 != tab.ID)
		{
			sql = MessageFormat.format("SELECT * FROM Tabs WHERE ID=\"{0}\";", tab.ID);
		}
		else 
		{
			sql = MessageFormat.format("SELECT * FROM Tabs WHERE CatID=\"{0}\" AND Name=\"{1}\";", tab.CatID, tab.Name);
		}
		
		try
		{
			ResultSet rs = db.executeQuery(sql);
			tab = LoadTabFromResult(tab, rs);
			
			return tab;
		}
		catch (Exception e) //tab doesn't exist
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns ArrayList of Tabs belonging to a category
	 * @param cat The category to be matched with
	 * @return ArrayList of Tab that belongs to cat
	 */
	public ArrayList<Tab> getTabs(Category cat)
	{
		ArrayList<Tab> list = new ArrayList<Tab>();
		
		try
		{
			String sql = MessageFormat.format("SELECT * FROM Tabs WHERE CatID=\"{0}\";",  cat.ID);
			ResultSet rs = db.executeQuery(sql);
			
			while (rs.next())
			{
				int id = rs.getInt("ID");
				int catID = rs.getInt("CatID");
				String name = rs.getString("Name");
				String info = rs.getString("Info");
				String date = rs.getString("Date");
				
				list.add(new Tab(id, name, catID, info, date));
			}
			rs.close();
		}
		catch (Exception e)
		{
			System.out.println (e.getMessage());
		}
		return list;
	}
	
	/**
	 * Fully constructs a Tab
	 * @param tab Tab to be fully constructed
	 * @param result ResultSet from database
	 * @return Fully Constructed Tab
	 * @throws SQLException
	 */
	private Tab LoadTabFromResult(Tab tab, ResultSet result) throws SQLException
	{
		tab.ID = result.getInt("ID");
		tab.CatID = result.getInt("CatID");
		tab.Name = result.getString("Name");
		tab.Info = result.getString("Info");
		tab.Date = result.getString("Date");
		
		return tab;
	}
	
	/**
	 * Saves tab into database
	 * @param tab Tab to be saved
	 * @param info Text inside the tab
	 */
	public void saveTab(Tab tab, String info)
	{
		try 
		{
			String sql = MessageFormat.format("UPDATE Tabs SET Info=\"{0}\" WHERE ID=\"{1}\";", info, tab.ID);
			db.executeUpdate(sql);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
