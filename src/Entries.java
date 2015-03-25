
/**
 * Entries.java
 * @author Alex
 *
 */

class Category 
{
	public int ID;
	public String Owner, Name;
	
	public Category(int id, String owner, String name)
	{
		ID = id;
		Owner = owner;
		Name = name;
	}
}

class Tab
{
	public int ID;
	public int CatID;
	public String Name, Info, Date;
	
	public Tab(int id, String name, int catID, String info, String date)
	{
		ID = id;
		Name = name;
		CatID = catID;
		Info = info;
		Date = date;
	}
}

class User
{
	public int ID;
	public String Name;
	public String Pass;
	
	public User(int id, String name, String password)
	{
		ID = id;
		Name = name;
		Pass = password;
	}
}
