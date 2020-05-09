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

import util.SimpleTableModel;

@SuppressWarnings("serial")
public class StaffDepartmentPanel extends JPanel {
	
	StaffInterface frame;
	JTable table;
	SimpleTableModel departments;
	
	public StaffDepartmentPanel(StaffInterface frame) {
		super(new BorderLayout());
		this.frame = frame;
		try {
			loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		table = new JTable(departments);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		JButton editNameB = new JButton("Edit Name");
		editNameB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					editName();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		controlPanel.add(editNameB);
		JButton changeHeadB = new JButton("Change Head Instructor");
		changeHeadB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					changeHead();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		controlPanel.add(changeHeadB);
		this.add(controlPanel, BorderLayout.SOUTH);
	}
	
	private void loadTable() throws SQLException {
		if(departments == null)
			departments = new SimpleTableModel(new String[] {"ID", "Name", "Head Instructor"});
		departments.clear();
		
		Statement st = frame.getConnection().createStatement();
		String sql = "SELECT * FROM College.Department";
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			String id = result.getString("department_id");
			String name = result.getString("name");
			String head = result.getString("head_instructor");
			departments.addRecord(new String[] {id, name, head});
		}
	}
	
	private void editName() throws SQLException {
		int index = table.getSelectedRow();
		if(index < 0) {
			JOptionPane.showMessageDialog(null, "Please choose a department!");
			return;
		}
		String depID = (String) departments.getValueAt(index, 0);
		
		String name = JOptionPane.showInputDialog("Enter the new name");
		if(name == null)
			return;
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("UPDATE College.Department SET name = '%s' WHERE department_id = '%s';", name, depID);
		st.executeUpdate(sql);
		
		JOptionPane.showMessageDialog(null, "Name changed!");
		loadTable();
	}
	
	private void changeHead() throws SQLException {
		int index = table.getSelectedRow();
		if(index < 0) {
			JOptionPane.showMessageDialog(null, "Please choose a department!");
			return;
		}
		String depID = (String) departments.getValueAt(index, 0);
		
		String head = JOptionPane.showInputDialog("Enter the new head instructor ID");
		if(head == null)
			return;
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("UPDATE College.Department SET head_instructor = '%s' WHERE department_id = '%s';", head, depID);
		st.executeUpdate(sql);
		
		JOptionPane.showMessageDialog(null, "Head instructor changed!");
		loadTable();
	}
	
	
}
