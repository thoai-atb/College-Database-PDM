package staffUI;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import util.UserUI;

@SuppressWarnings("serial")
public class StaffInterface extends UserUI {
	
	public StaffInterface(Connection connection, String staffID) {
		super(connection, staffID);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Student", new StaffUserPanel(this, "Person.Student", "student_id", "Student"));
		tabs.add("Instructor", new StaffUserPanel(this, "Person.Instructor", "instructor_id", "Instructor"));
		tabs.add("Department", new StaffDepartmentPanel(this));
		this.add(tabs);
	}

	@Override
	protected void loadAccount() {
		try {
			this.loadAccount("Person.Staff", "staff_id");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

}
