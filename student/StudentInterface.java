package student;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import general.LoginInterface;
import general.UserInfoInterface;
import general.UserInfoListener;

@SuppressWarnings("serial")
public class StudentInterface extends JFrame implements TableModelListener, UserInfoListener {
	
	private Connection connection;
	private CourseTableModel availableCourses;
	private CourseTableModel registeredCourses;
	private String studentID;
	private String infoID;
	
	public StudentInterface(Connection connection, String studentID) {
		this.studentID = studentID;
		this.connection = connection;		
		
		try {
			loadAccount();
			fillTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		JPanel availablePanel = new JPanel(new BorderLayout());
		availablePanel.setBorder(BorderFactory.createTitledBorder("Available Courses"));
		JTable table = new JTable(availableCourses);
		availablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel registeredPanel = new JPanel(new BorderLayout());
		registeredPanel.setBorder(BorderFactory.createTitledBorder("Registered Courses"));
		JTable table1 = new JTable(registeredCourses);
		registeredPanel.add(new JScrollPane(table1), BorderLayout.CENTER);
		
		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, availablePanel, registeredPanel);
		splitter.setResizeWeight(0.75);
		splitter.setContinuousLayout(true);
		
		this.setLayout(new BorderLayout());
		this.add(splitter, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		JButton refreshB = new JButton("Refresh");
		refreshB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fillTable();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		JButton cancelB = new JButton("Close");
		cancelB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					close();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		controlPanel.add(cancelB);
		JButton saveB = new JButton("Save");
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		controlPanel.add(saveB);
		controlPanel.add(refreshB);
		this.add(controlPanel, BorderLayout.SOUTH);
		
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
	
	private void loadAccount() throws SQLException {
		Statement st = connection.createStatement();
		String subsql = String.format("SELECT info_id FROM Person.Student WHERE student_id = '%s'", studentID);
		String sql = String.format("SELECT * FROM Person.UserInfo WHERE info_id = (%s);", subsql);
		ResultSet result = st.executeQuery(sql);
		result.next();
		infoID = result.getString("info_id");
		this.setTitle(result.getString("name") + " " + studentID.toUpperCase());
	}
	
	private void fillTable() throws SQLException {
		Statement st = connection.createStatement();
		String sql = "SELECT * FROM College.Course;";
		ResultSet result = st.executeQuery(sql);
		
		if(availableCourses == null) {
			availableCourses = new CourseTableModel();
			availableCourses.setCheckable(true);
			availableCourses.addTableModelListener(this);
		} else
			availableCourses.clear();
		
		while(result.next()) {
			String[] record = {result.getString(1), result.getString(2), result.getString(3), result.getString(4)};
			availableCourses.addRecord(record, false);
		}
		

		sql = String.format("SELECT * FROM College.Course c JOIN Enroll.Enrolled e ON c.course_id = e.course_id WHERE e.student_id = '%s';", studentID, studentID);
		result = st.executeQuery(sql);
		
		while(result.next())
			availableCourses.setSelected(result.getString("course_id"), true);
		
		if(registeredCourses == null) {
			registeredCourses = new CourseTableModel();
			updateRegisteredCourses();
		}
	}
	
	private void updateRegisteredCourses() {
		if(registeredCourses == null)
			return;

		registeredCourses.clear();
		
		for(Course c : availableCourses.getCourses())
			if(c.selected) {
				registeredCourses.addCourse(c);
			}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getSource() == availableCourses && registeredCourses != null) {
			updateRegisteredCourses();
		}
	}
	
	private void save() throws SQLException {
		Statement st = connection.createStatement();

		// First delete all the enroll record in the enrolled table
		String sql = String.format("DELETE FROM Enroll.Enrolled WHERE student_id = '%s';", studentID);
		st.executeUpdate(sql);
		
		// Iterate through the registered courses to insert to the table
		List<Course> courses = registeredCourses.getCourses();
		for(Course c : courses) {
			sql = String.format("INSERT INTO Enroll.Enrolled VALUES ('%s', '%s');", studentID, c.id); 
			st.executeUpdate(sql);
		}
		
		JOptionPane.showMessageDialog(null, "Registration Saved!");
	}
	
	private void close() throws SQLException {
		connection.close();
		this.dispose();
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
		try {
			loadAccount();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
