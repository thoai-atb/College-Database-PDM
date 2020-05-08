package studentUI;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import util.UserUI;

@SuppressWarnings("serial")
public class StudentInterface extends UserUI implements TableModelListener {
	
	private EnrollTableModel availableCourses;
	private EnrollTableModel registeredCourses;
	
	public StudentInterface(Connection connection, String studentID) {
		super(connection, studentID);
		
		try {
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
	}
	
	protected void loadAccount() {
		try {
			this.loadAccount("Person.Student", "student_id");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	private void fillTable() throws SQLException {
		Statement st = getConnection().createStatement();
		String sql = "SELECT * FROM College.Course;";
		ResultSet result = st.executeQuery(sql);
		
		if(availableCourses == null) {
			availableCourses = new EnrollTableModel();
			availableCourses.setCheckable(true);
			availableCourses.addTableModelListener(this);
		} else
			availableCourses.clear();
		
		while(result.next()) {
			String courseID = result.getString("course_id");
			String name = result.getString("name");
			String department =  result.getString("department_id");
			String instructorID = result.getString("instructor_id");
			boolean enrolled = false;
			availableCourses.addRecord(courseID, name, department, instructorID, enrolled);
		}
		

		sql = String.format("SELECT * FROM College.Course c JOIN Enroll.Enrolled e ON c.course_id = e.course_id WHERE e.student_id = '%s';", getUserID());
		result = st.executeQuery(sql);
		
		while(result.next())
			availableCourses.setSelected(result.getString("course_id"), true);
		
		if(registeredCourses == null) {
			registeredCourses = new EnrollTableModel();
			updateRegisteredCourses();
		}
	}
	
	private void updateRegisteredCourses() {
		if(registeredCourses == null)
			return;

		registeredCourses.clear();
		
		for(CourseRecord c : availableCourses.getCourses())
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
		Statement st = getConnection().createStatement();

		// First delete all the enroll record in the enrolled table
		String sql = String.format("DELETE FROM Enroll.Enrolled WHERE student_id = '%s';", getUserID());
		st.executeUpdate(sql);
		
		// Iterate through the registered courses to insert to the table
		List<CourseRecord> courses = registeredCourses.getCourses();
		for(CourseRecord c : courses) {
			sql = String.format("INSERT INTO Enroll.Enrolled VALUES ('%s', '%s');", getUserID().toUpperCase(), c.info[0]); 
			st.executeUpdate(sql);
		}
		
		JOptionPane.showMessageDialog(null, "Registration Saved!");
	}
}
