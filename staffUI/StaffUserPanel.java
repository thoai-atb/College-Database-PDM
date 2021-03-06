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
public class StaffUserPanel extends JPanel implements InfoListener {
	
	StaffInterface frame;
	JTable table;
	SimpleTableModel tableModel;
	String userTableName, userIDLabel, entityName;
	
	public StaffUserPanel(StaffInterface frame, String userTableName, String userIDLabel, String entityName) {
		super(new BorderLayout());
		this.frame = frame;
		this.userTableName = userTableName;
		this.userIDLabel = userIDLabel;
		this.entityName = entityName;
		
		try {
			loadTable();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		table = new JTable(tableModel);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		JButton addB = new JButton("Create " + entityName);
		addB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		controlPanel.add(addB);
		JButton removeB = new JButton("Delete " + entityName);
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
		JButton changedepB = new JButton("Change Department");
		changedepB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					changeDep();
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		controlPanel.add(changedepB);
		this.add(controlPanel, BorderLayout.SOUTH);
	}
	
	private void loadTable() throws SQLException {
		if(tableModel == null)
			tableModel = new SimpleTableModel(new String[] {"ID", "Name", "Department", "Info ID"});
		tableModel.clear();
		
		Statement st = frame.getConnection().createStatement();
		String sql = "SELECT * FROM " + userTableName + " A JOIN Person.UserInfo U ON A.info_id = U.info_id ORDER BY department_id;";
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			String id = result.getString(userIDLabel);
			String name = result.getString("name");
			String dep =  result.getString("department_id");
			String info =  result.getString("info_id");
			tableModel.addRecord(new String[] {id, name, dep, info});
		}
	}
	
	private void add() {
		new CreateAccountInterface(frame.getConnection(), this, userTableName, userIDLabel, entityName);
	}
	
	private void remove() throws SQLException {
		int index = table.getSelectedRow();
		if(index < 0) {
			JOptionPane.showMessageDialog(null, "Please choose the account to be deleted!");
			return;
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to delete this account? All data goes along with this account will be lost!");
		if(confirm != JOptionPane.OK_OPTION)
			return;
		
		String infoID = (String) tableModel.getValueAt(index, 3);
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("DELETE FROM Person.UserInfo WHERE info_id = '%s';", infoID);
		st.executeUpdate(sql);
		
		JOptionPane.showMessageDialog(null, "The account was deleted successfully!");
		infoChanged();
	}
	
	private void changeDep() throws SQLException {
		int index = table.getSelectedRow();
		if(index < 0) {
			JOptionPane.showMessageDialog(null, "Please choose the account to change the department!");
			return;
		}
		
		String depID = JOptionPane.showInputDialog("Enter the new department ID");
		if(depID == null)
			return;
		String userID = JOptionPane.showInputDialog("Enter the new " + entityName + " ID");
		if(userID == null)
			return;

		String infoID = (String) tableModel.getValueAt(index, 3);
		Statement st = frame.getConnection().createStatement();
		String sql = String.format("UPDATE %s SET department_id = '%s', %s = '%s' WHERE info_id = '%s';", userTableName, depID, userIDLabel, userID, infoID);
		st.executeUpdate(sql);
		
		JOptionPane.showMessageDialog(null, "Department changed!");
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
