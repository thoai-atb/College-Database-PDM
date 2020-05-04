package instructorUI;

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
import javax.swing.JTabbedPane;

import general.LoginInterface;
import general.UserInfoInterface;
import general.UserInfoListener;
import instructorUI.department.InstructorDepartmentPanel;
import instructorUI.teach.InstructorTeachPanel;

@SuppressWarnings("serial")
public class InstructorInterface extends JFrame implements UserInfoListener {
	
	Connection connection;
	String instructorID;
	InstructorTeachPanel panel1;
	InstructorDepartmentPanel panel2;
	
	private String infoID;
	
	public InstructorInterface(Connection connection, String instructorID) {
		this.connection = connection;
		this.instructorID = instructorID;
		
		try {
			loadAccount();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		JTabbedPane tp = new JTabbedPane();
		panel1 = new InstructorTeachPanel(this);
		panel2 = new InstructorDepartmentPanel(this);
		tp.add("Teach", panel1);
		tp.add("Department", panel2);
		this.add(tp);
		
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
		
		setSize(500, 500);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public String getInstructorID() {
		return instructorID;
	}
	
	
	private void logout() throws SQLException {
		new LoginInterface(connection);
		this.dispose();
	}
	
	private void editInfo() {
		new UserInfoInterface(connection, infoID, this);
	}
	
	private void loadAccount() throws SQLException {
		Statement st = connection.createStatement();
		String subsql = String.format("SELECT info_id FROM Person.Instructor WHERE instructor_id = '%s'", instructorID);
		String sql = String.format("SELECT * FROM Person.UserInfo WHERE info_id = (%s);", subsql);
		ResultSet result = st.executeQuery(sql);
		result.next();
		infoID = result.getString("info_id");
		this.setTitle(result.getString("name") + " " + instructorID.toUpperCase());
	}

	@Override
	public void infoChanged() {
		try {
			loadAccount();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void reload() {
		try {
			panel1.loadTable();
			panel2.loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
