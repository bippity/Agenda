import javax.swing.*;

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
	private JPanel rightPanel = new JPanel();
	private JButton catButton = new JButton("Create Category");
	private JButton tabButton = new JButton("Create Tab");
	private JComboBox<Category> categories = new JComboBox<Category>();
	public MainFrame(User u)
	{
		user = u;
		System.out.println(user.toString());
		initiateDB();
		
		setSize(1000, 500);
		setTitle("Agenda");
		
		ArrayList<Category> catList = dbManager.getCategories();
		for (Category c : catList)
		{
			categories.addItem(c);
		}
		
		add(leftPanel, BorderLayout.WEST);
		JLabel label = new JLabel("Category");
		categoryInput = new JTextField("To-Do");
		leftPanel.add(label, BorderLayout.NORTH);
		leftPanel.add(categoryInput);
		leftPanel.add(catButton);
		leftPanel.add(categories);
		
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
}
