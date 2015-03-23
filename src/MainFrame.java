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
	private JPanel leftSouthPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JButton catButton = new JButton("Create Category");
	private JButton tabButton = new JButton("Create Tab");
	private JButton logoutButton = new JButton("Logout");
	private JComboBox<String> categories = new JComboBox<String>();
	
	public MainFrame(User u)
	{
		user = u;
		System.out.println(user.toString());
		initiateDB();
		
		setSize(1000, 500);
		setTitle("Agenda");
		
		setLayout(new BorderLayout());
		add(leftPanel, BorderLayout.WEST);
		
		//JLabel catlabel = new JLabel("Categories");
		categoryInput = new JTextField("To-Do");
		
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBorder(new TitledBorder("Categories"));
		leftPanel.add(categories, BorderLayout.NORTH);
		leftPanel.add(leftSouthPanel, BorderLayout.SOUTH);
		leftSouthPanel.setLayout(new GridLayout(0, 1));
		//leftSouthPanel.add(new JLabel("test"),BorderLayout.SOUTH);
		
		leftSouthPanel.setBorder(new TitledBorder("Create"));
		leftSouthPanel.add(categoryInput);
		leftSouthPanel.add(catButton);
		
		add(new JSeparator(JSeparator.VERTICAL), BorderLayout.CENTER);
		
		add(rightPanel, BorderLayout.EAST);
		
		catButton.addActionListener(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		JButton b = (JButton)e.getSource();
		if (b.equals(catButton))
		{
			try
			{
				dbManager.InsertCategory(user, categoryInput.getText());
			}
			catch (Exception ex)
			{
				System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
				System.exit(0);
			}
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
		ArrayList<Category> catList = dbManager.getCategories(user);
		for (Category c : catList)
		{
			categories.addItem(c.Name);
		}
	}
}
