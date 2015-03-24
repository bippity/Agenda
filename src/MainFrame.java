import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.util.*;
import java.sql.*;

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
	private JPanel tabInfo = new JPanel();
	
	private JButton catButton = new JButton("Create Category");
	private JButton tabButton = new JButton("Create Tab");
	private JButton logoutButton = new JButton("Logout");
	private JComboBox<String> categories = new JComboBox<String>();
	private JComboBox<String> tabs = new JComboBox<String>();
	
	private JTextArea tabDisplay = new JTextArea(50, 20);
	
	public MainFrame(User u)
	{
		user = u;
		System.out.println(user.toString());
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
		logoutButton.setForeground(new Color(190,0,0));
		leftCenterPanel.add(logoutButton, BorderLayout.SOUTH);
		
		
		//leftSouthPanel.add(new JLabel("test"),BorderLayout.SOUTH);
		leftSouthPanel.setLayout(new GridLayout(0, 1));
		leftSouthPanel.setBorder(new TitledBorder("Create Category"));
		leftSouthPanel.add(new JLabel("Name"));
		categoryInput = new JTextField("To-Do");
		leftSouthPanel.add(categoryInput);
		leftSouthPanel.add(catButton);
		//leftSouthPanel.add(new JPanel()); //creates gap between creating category and tab
//		leftSouthPanel.add(new JLabel("Tab Name"));
//		
//		tabInput = new JTextField("Finish HW");
//		leftSouthPanel.add(tabInput);
//		leftSouthPanel.add(tabButton);
		
		
		add(rightPanel);
		rightPanel.setLayout(new BorderLayout());
		//rightPanel.setBorder(new TitledBorder("Right Panel"));
		rightPanel.add(rightWestPanel, BorderLayout.WEST);
		rightPanel.add(rightEastPanel);
		
		rightEastPanel.setLayout(new BorderLayout());
		rightEastPanel.setBorder(new TitledBorder("Displayed Tab"));
		rightEastPanel.add(tabInfo, BorderLayout.NORTH);
		rightEastPanel.add(tabDisplay);
		rightEastPanel.add(new JPanel(), BorderLayout.SOUTH);
		
		tabInfo.setLayout(new BorderLayout());
		//tabInfo.setBorder(new TitledBorder("tabInfo"));
		tabInfo.add(new JLabel("Tab ID: 1"), BorderLayout.WEST);
		tabInfo.add(new JLabel("Date Created: 3/24/2015"), BorderLayout.EAST);
		
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
		
		//rightPanel.add(new JTextField("Blah", 20));
		
		catButton.addActionListener(this);
		tabButton.addActionListener(this);
		logoutButton.addActionListener(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(catButton))
		{
			try
			{
				dbManager.InsertCategory(user, categoryInput.getText());
				update();
			}
			catch (Exception ex)
			{
				System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
				System.exit(0);
			}
		}
		else if (e.getSource().equals(tabButton))
		{
			
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
			c = DriverManager.getConnection("jdbc:sqlite:test.sqlite");
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
		ArrayList<Category> catList = dbManager.getCategories(user);
		for (Category c : catList)
		{
			categories.addItem(c.Name);
		}
		
		tabs.removeAllItems();
		ArrayList<Tab> tabList = dbManager.getTabs(user);
		for (Tab t : tabList)
		{
			String info = t.Info;
			if (info.length() > 10)
				info = info.substring(0,9);
			tabs.addItem(info);
		}
	}
	
	public void logout()
	{
		try
		{
			int choice = JOptionPane.showConfirmDialog(getRootPane(), "Are you sure you want to logout?", "Confirm Logout", JOptionPane.INFORMATION_MESSAGE);
			
			if (choice == JOptionPane.YES_OPTION)
			{
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
