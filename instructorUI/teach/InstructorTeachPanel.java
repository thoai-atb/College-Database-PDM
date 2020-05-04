package instructorUI.teach;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import instructorUI.InstructorInterface;

@SuppressWarnings("serial")
public class InstructorTeachPanel extends JPanel {

	private TeachCourseTableModel teachCourses;
	private JTable teachCoursesTable;
	private InstructorInterface frame;
	
	public InstructorTeachPanel(InstructorInterface frame) {
		super(new BorderLayout());
		
		this.frame = frame;
		
		try {
			loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		this.setBorder(BorderFactory.createTitledBorder("Courses You Teach"));
		teachCoursesTable = new JTable(teachCourses);
		this.add(new JScrollPane(teachCoursesTable), BorderLayout.CENTER);
		
		// BUTTONS
		JPanel controlPanel = new JPanel();
		JButton seeStudentListB = new JButton("View Student List");
		seeStudentListB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewStudentList();
			}
		});
		controlPanel.add(seeStudentListB);
		this.add(controlPanel, BorderLayout.SOUTH);
	}
	

	private void viewStudentList() {
		int i = teachCoursesTable.getSelectedRow();
		if(i < 0) {
			JOptionPane.showMessageDialog(null, "Please choose a course!");
			return;
		}
		String cid = (String) teachCourses.getValueAt(i, 0);
		String cname = (String) teachCourses.getValueAt(i, 1);
		new StudentListInterface(frame.getConnection(), cid, cname);
	}
	
	public void loadTable() throws SQLException {
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("SELECT * FROM College.Course WHERE instructor_id = '%s';", frame.getInstructorID());
		ResultSet result = st.executeQuery(sql);
		
		if(teachCourses == null) {
			teachCourses = new TeachCourseTableModel();
		} else
			teachCourses.clear();
		
		while(result.next()) {
			String courseID = result.getString("course_id");
			String name = result.getString("name");
			String department = result.getString("department_id");
			String instructor = result.getString("instructor_id");
			teachCourses.addRecord(courseID, name, department, instructor);
		}
	}

}
