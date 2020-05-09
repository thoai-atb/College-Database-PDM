package staffUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import util.SimpleTableModel;

@SuppressWarnings("serial")
public class StaffInstructorPanel extends JPanel {
	
	StaffInterface frame;
	JTable table;
	SimpleTableModel instructors;
	
	public StaffInstructorPanel(StaffInterface frame) {
		super(new BorderLayout());
		this.frame = frame;
		
		try {
			loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		JPanel controlPanel = new JPanel();
		JButton addB = new JButton("Create Student");
		addB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		controlPanel.add(addB);
		JButton removeB = new JButton("Delete Student");
		removeB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					remove();
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		controlPanel.add(removeB);
		this.add(controlPanel, BorderLayout.SOUTH);
	}
	
	private void loadTable() throws SQLException {
		if(instructors == null)
			instructors = new SimpleTableModel(new String[] {"ID", "Name", "Department", "Info ID"});
		instructors.clear();
		
		Statement st = frame.getConnection().createStatement();
		String sql = "SELECT * FROM Person.Instructor I JOIN Person.UserInfo U ON I.info_id = U.info_id ORDER BY department_id;";
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			String id = result.getString("instructor_id");
			String name = result.getString("name");
			String dep =  result.getString("department_id");
			String info =  result.getString("info_id");
			instructors.addRecord(new String[] {id, name, dep, info});
		}
	}
	
	private void add() {
		
	}
	
	private void remove() throws SQLException {
		
	}
}
