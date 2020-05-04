package instructorUI.teach;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class StudentListInterface extends JFrame {
	public StudentListInterface(Connection connection, String courseID, String courseName) {
		super(courseName + " - " + courseID);
		this.setLayout(new BorderLayout());
		StudentTableModel model = new StudentTableModel();
		try {
			loadTable(model, courseID, connection);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		JTable table = new JTable(model);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		setSize(600, 300);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	private void loadTable(StudentTableModel model, String courseID, Connection connection) throws SQLException {
		Statement st = connection.createStatement();
		String sql = String.format("SELECT * FROM Enroll.Enrolled E JOIN Person.Student S ON E.student_id = S.student_id AND E.course_id = '%s' JOIN Person.UserInfo U ON S.info_id = U.info_id;", courseID);
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			String studentID = result.getString("student_id");
			String studentName = result.getString("name");
			String department = result.getString("department_id");
			String phone = result.getString("phone_number");
			String email = result.getString("email");
			model.addRecord(studentID, studentName, department, phone, email);
		}
	}
}
