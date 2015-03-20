
class Category 
{
	public int ID;
	public String Owner, Name, TabID;
	
	public Category(int id, String owner, String name, String tabID)
	{
		ID = id;
		Owner = owner;
		Name = name;
		TabID = tabID;
	}
}

class Tab
{
	public int ID;
	public String Info, Date;
	
	public Tab(int id, String info, String date)
	{
		ID = id;
		Info = info;
		Date = date;
	}
}

class User
{
	public int ID;
	public String Name;
	public String Pass;
	public String catID;
	
	public User(int id, String name, String password, String catIDs)
	{
		ID = id;
		Name = name;
		Pass = password;
		catID = catIDs;
	}
	
	public String toString()
	{
		return "ID: " + ID + "\nName: " + Name + "\nPass: " + Pass + "\nCategory IDs: " + catID;
	}
}
