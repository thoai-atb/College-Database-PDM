package instructorUI;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import instructorUI.department.InstructorDepartmentPanel;
import instructorUI.teach.InstructorTeachPanel;
import util.UserUI;

@SuppressWarnings("serial")
public class InstructorInterface extends UserUI {
	
	InstructorTeachPanel panel1;
	InstructorDepartmentPanel panel2;
	
	public InstructorInterface(Connection connection, String instructorID) {
		super(connection, instructorID);
		
		JTabbedPane tp = new JTabbedPane();
		panel1 = new InstructorTeachPanel(this);
		panel2 = new InstructorDepartmentPanel(this);
		tp.add("Teach", panel1);
		tp.add("Department", panel2);
		this.add(tp);
	}
	
	public void reload() {
		try {
			panel1.loadTable();
			panel2.loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@Override
	protected void loadAccount() {
		try {
			this.loadAccount("Person.Instructor", "instructor_id");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
