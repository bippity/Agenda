import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

/**
 * MainFrame.java
 * @author Alex
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener
{
	public static Database dbManager;
	private static Connection c;
	
	private User user;
	
	public JTextField categoryInput;
	public JTextField tabInput;
	
	private JPanel leftPanel = new JPanel();
	private JPanel leftCenterPanel = new JPanel();
	private JPanel leftSouthPanel = new JPanel();
	
	private JPanel rightPanel = new JPanel();
	private JPanel rightWestPanel = new JPanel();
	private JPanel rightEastPanel = new JPanel();
	
	private JPanel innerTabsPanel = new JPanel();
	private JPanel tabDisplay = new JPanel();
	
	private JButton catButton = new JButton("Create Category");
	private JButton tabButton = new JButton("Create Tab");
	private JButton delCatButton = new JButton("Delete Category");
	private JButton delTabButton = new JButton("Delete Tab");
	private JButton saveButton = new JButton("Save");
	private JButton logoutButton = new JButton("Logout");
	
	private JComboBox<String> categories = new JComboBox<String>();
	private JComboBox<String> tabs = new JComboBox<String>();
	
	private JTextArea tabInfo = new JTextArea(50, 20);
	
	private Category currentCat;
	private Tab currentTab;
	
	private ArrayList<Category> catList;
	private ArrayList<Tab> tabList;
	
	private JLabel tabName = new JLabel("Tab Name: ");
	private JLabel tabDate = new JLabel("Date Created: ");
	
	/**
	 * Constructor
	 * @param u User that logged in
	 */
	public MainFrame(User u)
	{
		user = u;
		initiateDB();
		update();
		
		setSize(1000, 500);
		setTitle("-Agenda-    User: " + user.Name);
		
		setLayout(new BorderLayout());
		add(leftPanel, BorderLayout.WEST);
		
		
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBorder(new TitledBorder("Categories"));
		leftPanel.add(categories, BorderLayout.NORTH);
		leftPanel.add(leftCenterPanel, BorderLayout.CENTER);
		leftPanel.add(leftSouthPanel, BorderLayout.SOUTH);
		
		leftCenterPanel.setLayout(new BorderLayout());
		leftCenterPanel.setBorder(new TitledBorder("Controls"));
		leftCenterPanel.add(logoutButton, BorderLayout.SOUTH);
		JPanel innerControls = new JPanel(new BorderLayout());
		leftCenterPanel.add(innerControls, BorderLayout.CENTER);
		innerControls.add(delTabButton, BorderLayout.NORTH);
		innerControls.add(saveButton, BorderLayout.SOUTH);
		logoutButton.setForeground(new Color(190,0,0));
		saveButton.setForeground(new Color(0, 145, 45));
		leftCenterPanel.add(delCatButton, BorderLayout.NORTH);
		
		
		
		//leftSouthPanel.add(new JLabel("test"),BorderLayout.SOUTH);
		leftSouthPanel.setLayout(new GridLayout(0, 1));
		leftSouthPanel.setBorder(new TitledBorder("Create Category"));
		leftSouthPanel.add(new JLabel("Name"));
		categoryInput = new JTextField("To-Do");
		leftSouthPanel.add(categoryInput);
		leftSouthPanel.add(catButton);
		
		
		add(rightPanel);
		rightPanel.setLayout(new BorderLayout());
		//rightPanel.setBorder(new TitledBorder("Right Panel"));
		rightPanel.add(rightWestPanel, BorderLayout.WEST);
		rightPanel.add(rightEastPanel);
		
		rightEastPanel.setLayout(new BorderLayout());
		//rightEastPanel.setBorder(new TitledBorder("Displayed Tab"));
		rightEastPanel.add(tabDisplay, BorderLayout.NORTH);
		rightEastPanel.add(tabInfo);
		rightEastPanel.add(new JPanel(), BorderLayout.SOUTH);
		
		tabDisplay.setLayout(new BorderLayout());
		//tabDisplay.setBorder(new TitledBorder("tabDisplay"));
		tabDisplay.add(tabName, BorderLayout.WEST);
		tabDisplay.add(tabDate, BorderLayout.EAST);
		
		rightWestPanel.setLayout(new BorderLayout());
		rightWestPanel.setBorder(new TitledBorder("Tabs"));
		rightWestPanel.add(tabs, BorderLayout.NORTH);
		rightWestPanel.add(innerTabsPanel, BorderLayout.SOUTH);
		
		innerTabsPanel.setLayout(new GridLayout(0,1));
		innerTabsPanel.setBorder(new TitledBorder("Create Tab"));
		innerTabsPanel.add(new JLabel("Name"));
		tabInput = new JTextField("Finish HW");
		innerTabsPanel.add(tabInput);
		innerTabsPanel.add(tabButton);
		
		
		categories.addActionListener(this);
		tabs.addActionListener(this);
		catButton.addActionListener(this);
		delCatButton.addActionListener(this);
		tabButton.addActionListener(this);
		delTabButton.addActionListener(this);
		saveButton.addActionListener(this);
		logoutButton.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Listens to actions performed
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(catButton)) //create Category button
		{
			if (categoryInput.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(getRootPane(), "You must give your category a name!", "Error", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			Category cat = new Category(0, user.Name, categoryInput.getText());
			
			if (dbManager.getCategoryByName(cat.Name, user) == null)
			{
				try
				{
					dbManager.InsertCategory(user, cat);
					update();
				}
				catch (Exception ex)
				{
					System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
				}
			}
			else 
			{
				JOptionPane.showMessageDialog(getRootPane(), "Category \"" + categoryInput.getText() + "\" already exists!", "Existing Category", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getSource().equals(delCatButton))
		{
			if (currentCat == null)
			{
				JOptionPane.showMessageDialog(getRootPane(), "There is no category to delete.", "Error", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			try
			{
				JOptionPane.showMessageDialog(getRootPane(), "Category \"" + currentCat.Name + "\" has been deleted!", "Category Deleted",
						JOptionPane.INFORMATION_MESSAGE);
				dbManager.deleteCategory(currentCat);
				update();
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		else if (e.getSource().equals(tabButton))
		{
			if (currentCat == null) //if a category doesn't exist
			{
				JOptionPane.showMessageDialog(getRootPane(), "You must create a category first!", "Error", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if (tabInput.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(getRootPane(), "You must give your tab a name!", "Error", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Tab tab = new Tab(0, tabInput.getText(), currentCat.ID, null, null);
			if (dbManager.getTabByName(tab.Name, tab.CatID) == null)
			{
				try
				{
					dbManager.InsertTab(tabInput.getText(), tab.CatID);
					update();
				}
				catch (Exception ex)
				{
					System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
				}
			}
			else 
			{
				JOptionPane.showMessageDialog(getRootPane(), "Tab \"" + tabInput.getText() + "\" already exists!", "Existing Tab", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getSource().equals(delTabButton))
		{
			if (currentTab == null)
			{
				JOptionPane.showMessageDialog(getRootPane(), "There is no tab to delete.", "Error", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			try
			{
				JOptionPane.showMessageDialog(getRootPane(), "Tab \"" + currentTab.Name + "\" has been deleted!", "Tab Deleted", JOptionPane.INFORMATION_MESSAGE);
				dbManager.deleteTab(currentTab);
				update();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}
		}
		else if (e.getSource().equals(categories))
		{
			String name = (String)categories.getSelectedItem();
			for (Category c : catList)
			{
				if (c.Name.equals(name))
				{
					currentCat = c;
					update();
					return;
				}
			}
		}
		else if (e.getSource().equals(tabs))
		{
			String name = (String)tabs.getSelectedItem();
			for (Tab t : tabList)
			{
				if (t.Name.equals(name))
				{
					currentTab = t;
					update();
					return;
				}
			}
		}
		else if (e.getSource().equals(saveButton))
		{
			if (currentTab == null)
			{
				JOptionPane.showMessageDialog(getRootPane(), "A tab must be opened/created before saving!", "Error", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			dbManager.saveTab(currentTab, tabInfo.getText());
			JOptionPane.showMessageDialog(getRootPane(), "Tab \"" + currentTab.Name + "\" has been saved!", "Tab Saved",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if (e.getSource().equals(logoutButton))
		{
			logout();
		}
	}
	
	public static void initiateDB()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:agenda.sqlite");
			dbManager = new Database(c);
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	public void update() //refreshes the tables/display
	{
		categories.removeAllItems();
		catList = dbManager.getCategories(user);
		for (Category c : catList)
		{
			categories.addItem(c.Name);
		}
		
		if (!catList.isEmpty())
		{
			if (currentCat == null)
			{
				currentCat = catList.get(0);
			}
			else
			{
				categories.setSelectedItem(currentCat.Name);
			}
		}
		else
		{
			currentCat = null;
		}
		
		tabs.removeAllItems();
		tabList = dbManager.getTabs(currentCat);
		for (Tab t : tabList)
		{
			tabs.addItem(t.Name);
		}
		
		if (!tabList.isEmpty())
		{
			if (currentTab == null)
			{
				currentTab = tabList.get(0);
			}
			else 
			{
				tabs.setSelectedItem(currentTab.Name);
			}
			tabName.setText("Tab Name: " + currentTab.Name);
			tabDate.setText("Date Created: " + currentTab.Date);
			tabInfo.setText(currentTab.Info);
		}
		else 
		{
			currentTab = null;
		}
	}
	
	/**
	 * Returns the user back to the LoginFrame
	 */
	public void logout()
	{
		try
		{
			int choice = JOptionPane.showConfirmDialog(getRootPane(), "Are you sure you want to logout?", "Confirm Logout", JOptionPane.INFORMATION_MESSAGE);
			
			if (choice == JOptionPane.YES_OPTION)
			{
				@SuppressWarnings("unused")
				LoginFrame main = new LoginFrame();
				dispose();
				c.close();
			}
		}
		catch (Exception e)
		{
			System.out.println (e.getMessage());
		}
	}
}
