import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.sql.*;

@SuppressWarnings({ "serial", "unused" })
public class LoginFrame extends JFrame implements ActionListener
{
	public JTextField username = new JTextField();
	public JTextField pass = new JTextField();
	private JPanel inputPanel = new JPanel();
	private JPanel southPanel = new JPanel();
	private JButton loginButton = new JButton("Login");
	private JButton newButton = new JButton("Create as new user");
	
	private static Connection c;
	private static Database dbManager;
	
	
	public LoginFrame()
	{
		//super("Agenda -Login-");
		initiateDB();
		
		setSize(300,200);
		setTitle("Agenda -Login-");
		
		add(southPanel, BorderLayout.SOUTH);
		southPanel.add(loginButton, BorderLayout.WEST);
		southPanel.add(newButton, BorderLayout.EAST);
		
		inputPanel.setLayout(new GridLayout(4,2));
		add(inputPanel);
		inputPanel.add(new JLabel("User: "));
		inputPanel.add(username);
		inputPanel.add(new JLabel("Pass: "));
		inputPanel.add(pass);
		
		loginButton.addActionListener(this);
		newButton.addActionListener(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JButton b = (JButton)e.getSource();
		
		if (b.equals(loginButton))
		{
			if (username.getText().isEmpty() || pass.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(getRootPane(), "Username and Password cannot be empty!", "Login Fail", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else 
			{
				try
				{
					User user = dbManager.getUserByName(username.getText());
					if (user == null)
					{
						JOptionPane.showMessageDialog(getRootPane(), "Username does not exist!", "Login Fail", 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					else //username exists
					{
						if (user.Pass.equals(pass.getText())) //if pass matches, start main frame
						{
							login(user);
							//c.close();
						}
						else
							JOptionPane.showMessageDialog(getRootPane(), "Incorrect Password!", "Login Fail", 
									JOptionPane.ERROR_MESSAGE);
					}
				}
				catch (Exception ex)
				{
					System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		    		System.exit(0);
				}
			}
		}
		else if (b.equals(newButton)) //create new account Button
		{
			if (username.getText().isEmpty() || pass.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(getRootPane(), "Username \"" + username.getText() + "\" already exists!", "Login Fail", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			else 
			{
				int choice = JOptionPane.showConfirmDialog(getRootPane(), "Are you sure?", "Confirm Account", JOptionPane.INFORMATION_MESSAGE);
				
				if (choice == JOptionPane.YES_OPTION)
				{
					User user = new User(0, username.getText(), pass.getText(), null);
	
					if (dbManager.getUserByName(user.Name)== null)
					{
						dbManager.AddUser(user);
						JOptionPane.showMessageDialog(getRootPane(), "User: " + user.Name + "\nPass: " + user.Pass, "Account Created",
								JOptionPane.INFORMATION_MESSAGE);
						login(user);
					}
					else 
					{
						JOptionPane.showMessageDialog(getRootPane(), "Username \"" + username.getText() + "\" already exists!", "Login Fail", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
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
	
	public void login(User user)
	{
		MainFrame main = new MainFrame(user);
		dispose();
	}
}
