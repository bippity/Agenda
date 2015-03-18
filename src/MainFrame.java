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
	private JPanel main = new JPanel();
	private JButton catButton = new JButton("Create Category");
	private JButton tabButton = new JButton("Create Tab");
	
	public MainFrame(User u)
	{
		user = u;
		System.out.println(user.toString());
		initiateDB();
		
		setSize(1000, 500);
		setTitle("Agenda");
		
		categoryInput = new JTextField("To-Do");
		
		add(main);
		
		JLabel label = new JLabel("Category");
		main.add(label);
		main.add(categoryInput);
		main.add(catButton);
		
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
