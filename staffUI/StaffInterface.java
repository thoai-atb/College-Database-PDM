package staffUI;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import util.UserUI;

@SuppressWarnings("serial")
public class StaffInterface extends UserUI {
	
	public StaffInterface(Connection connection, String staffID) {
		super(connection, staffID);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Student", new StaffStudentPanel(this));
		tabs.add("Instructor", new JPanel());
		tabs.add("Department", new JPanel());
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
