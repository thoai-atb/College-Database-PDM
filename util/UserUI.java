package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import general.LoginInterface;

@SuppressWarnings("serial")
public abstract class UserUI extends JFrame implements InfoListener {

	private Connection connection;
	private String userID;
	private String infoID;
	
	public UserUI(Connection connection, String userID) {
		this.connection = connection;
		this.userID = userID;
		
		// MENU
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Profile");
		JMenuItem editInfoM = new JMenuItem("User Info");
		editInfoM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInfo();
			}
		});
		menu.add(editInfoM);
		JMenuItem logoutM = new JMenuItem("Log Out");
		logoutM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					logout();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		menu.add(logoutM);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		loadAccount();

		setSize(500, 500);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	protected abstract void loadAccount();
	
	protected void loadAccount(String userTableName, String userIDLabel) throws SQLException {
		Statement st = connection.createStatement();
		String subsql = String.format("SELECT info_id FROM %s WHERE %s = '%s'", userTableName, userIDLabel, userID);
		String sql = String.format("SELECT * FROM Person.UserInfo WHERE info_id = (%s);", subsql);
		ResultSet result = st.executeQuery(sql);
		result.next();
		infoID = result.getString("info_id");
		this.setTitle(result.getString("name") + " " + userID.toUpperCase());
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getInfoID() {
		return infoID;
	}
	
	private void logout() throws SQLException {
		new LoginInterface(connection);
		this.dispose();
	}
	
	private void editInfo() {
		new UserInfoInterface(connection, infoID, this);
	}
	
	@Override
	public void infoChanged() {
		loadAccount();
	}
}
