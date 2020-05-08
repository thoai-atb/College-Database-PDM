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
import javax.swing.JScrollPane;
import javax.swing.JTable;

import util.InfoListener;
import util.SimpleTableModel;

@SuppressWarnings("serial")
public class StaffStudentPanel extends JPanel implements InfoListener {
	
	StaffInterface frame;
	JTable table;
	SimpleTableModel students;
	
	public StaffStudentPanel(StaffInterface frame) {
		super(new BorderLayout());
		this.frame = frame;
		
		try {
			loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		table = new JTable(students);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		JButton addB = new JButton("Create new Student");
		addB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		controlPanel.add(addB);
		JButton removeB = new JButton("Delete selected Student");
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
	
	public void loadTable() throws SQLException {
		if(students == null)
			students = new SimpleTableModel(new String[] {"ID", "Name", "Department", "Info ID"});
		students.clear();
		
		Statement st = frame.getConnection().createStatement();
		String sql = "SELECT * FROM Person.Student S JOIN Person.UserInfo U ON S.info_id = U.info_id ORDER BY department_id;";
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			String id = result.getString("student_id");
			String name = result.getString("name");
			String dep =  result.getString("department_id");
			String info =  result.getString("info_id");
			students.addRecord(new String[] {id, name, dep, info});
		}
	}
	
	private void add() {
		new CreateStudentInterface(frame.getConnection(), this);
	}
	
	private void remove() throws SQLException {
		int index = table.getSelectedRow();
		if(index < 0) {
			JOptionPane.showMessageDialog(null, "Please choose the student to be deleted!");
			return;
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to delete this student? All data goes along with this account will be lost!");
		if(confirm != JOptionPane.OK_OPTION)
			return;
		
		String infoID = (String) students.getValueAt(index, 3);
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("DELETE FROM Person.UserInfo WHERE info_id = '%s';", infoID);
		st.executeUpdate(sql);
		
		JOptionPane.showMessageDialog(null, "The account was deleted successfully!");
		infoChanged();
	}

	@Override
	public void infoChanged() {
		try {
			loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
