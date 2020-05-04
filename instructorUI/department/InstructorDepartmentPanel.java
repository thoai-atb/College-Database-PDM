package instructorUI.department;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import instructorUI.InstructorInterface;
import instructorUI.teach.TeachCourseTableModel;

@SuppressWarnings("serial")
public class InstructorDepartmentPanel extends JPanel implements AddCourseListener {
	
	private InstructorInterface frame;
	private String departmentID, departmentName;
	private TeachCourseTableModel departmentCourses;
	private JTable courseTable;
	
	public InstructorDepartmentPanel(InstructorInterface frame) {
		super(new BorderLayout());
		this.frame = frame;
		try {
			loadDepartment();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		

		JLabel messageLabel = new JLabel();
		JPanel titlePanel = new JPanel();
		titlePanel.add(messageLabel);
		this.add(titlePanel, BorderLayout.NORTH);
		
		if(departmentID == null) {
			messageLabel.setText("You are not the head instructor of any department");
		} else {
			messageLabel.setText("You are the head instructor of department " + departmentID + " (" + departmentName + ")");
			departmentCourses = new TeachCourseTableModel();
			try {
				loadTable();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			courseTable = new JTable(departmentCourses);
			JPanel coursePanel = new JPanel(new BorderLayout());
			coursePanel.setBorder(BorderFactory.createTitledBorder("Department's Courses"));
			coursePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
			this.add(coursePanel, BorderLayout.CENTER);
			
			// Buttons
			JPanel controlPanel = new JPanel();
			JButton addCourseB = new JButton("Add Course");
			addCourseB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addCourse();
				}
			});
			controlPanel.add(addCourseB);
			JButton removeCourseB = new JButton("Remove Course");
			removeCourseB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						removeCourse();
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
			});
			controlPanel.add(removeCourseB);
			this.add(controlPanel, BorderLayout.SOUTH);
		}
	}
	
	private void addCourse() {
		AddCourseInterface f = new AddCourseInterface(frame.getConnection(), departmentID);
		f.setListener(this);
	}
	
	private void removeCourse() throws SQLException {
		int i = courseTable.getSelectedRow();
		if(i < 0) {
			JOptionPane.showMessageDialog(null, "Please choose a course!");
			return;
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to remove this course? All the registration in this course will be deleted, this cannot be undone!");
		if(confirm != JOptionPane.OK_OPTION)
			return;
		
		String cid = (String) departmentCourses.getValueAt(i, 0);
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("DELETE FROM College.Course WHERE course_id = '%s';", cid);
		st.executeUpdate(sql);
		
		frame.reload();
	}
	
	private void loadDepartment() throws SQLException {
		departmentID = null;
		departmentName = null;
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("SELECT * FROM College.Department WHERE head_instructor = '%s';", frame.getInstructorID());
		ResultSet result = st.executeQuery(sql);
		if(result.next()) {
			departmentID = result.getString("department_id");
			departmentName = result.getString("name");
		}
	}
	
	public void loadTable() throws SQLException {
		departmentCourses.clear();
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("SELECT * FROM College.Course WHERE department_id = '%s';", departmentID);
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			String courseID = result.getString("course_id");
			String name = result.getString("name");
			String department = result.getString("department_id");
			String instructor = result.getString("instructor_id");
			departmentCourses.addRecord(courseID, name, department, instructor);
		}
	}

	@Override
	public void courseAdded() {
		frame.reload();
	}

}
