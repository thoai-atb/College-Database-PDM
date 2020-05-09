package staffUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import util.InfoListener;

@SuppressWarnings("serial")
public class CreateAccountInterface extends JFrame {
	
	JTextField idField, nameField, depField;
	Connection connection;
	InfoListener listener;
	String userTableName, userIDLabel, entityName;
	
	public CreateAccountInterface(Connection connection, InfoListener listener, String userTableName, String userIDLabel, String entityName) {
		super("Create New " + entityName);
		this.connection = connection;
		this.listener = listener;
		this.userTableName = userTableName;
		this.userIDLabel = userIDLabel;
		this.entityName = entityName;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 10;
		c.weighty = 10;
		
		JLabel idLabel = new JLabel("ID");
		c.gridx = 0;
		c.gridy = 0;
		this.add(idLabel, c);
		
		idField = new JTextField(20);
		c.gridx = 1;
		c.gridy = 0;
		this.add(idField, c);
		
		JLabel nameLabel = new JLabel("Name");
		c.gridx = 0;
		c.gridy = 1;
		this.add(nameLabel, c);
		
		nameField = new JTextField(20);
		c.gridx = 1;
		c.gridy = 1;
		this.add(nameField, c);
		
		JLabel depLabel = new JLabel("Department");
		c.gridx = 0;
		c.gridy = 2;
		this.add(depLabel, c);
		
		depField = new JTextField(20);
		c.gridx = 1;
		c.gridy = 2;
		this.add(depField, c);
		
		JButton createB = new JButton("Create");
		createB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					create();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		this.add(createB, c);
		
		this.setVisible(true);
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
	}
	
	private void create() throws SQLException {
		// Step 1: Insert the new user info record
		Statement st = connection.createStatement();
		String name = nameField.getText();
		String sql = String.format("INSERT INTO Person.UserInfo (name) VALUES ('%s');", name);
		st.executeUpdate(sql);
		
		// Step 2: Get the info_id from that record
		sql = "SELECT SCOPE_IDENTITY();";
		ResultSet rs = st.executeQuery(sql);
		rs.next();
		String infoID = rs.getString(1);

		// Step 3: Add the new user record
		String id = idField.getText();
		String dep = depField.getText();
		sql = String.format("INSERT INTO %s (info_id, %s, department_id) VALUES ('%s', '%s', '%s');", userTableName, userIDLabel, infoID, id, dep);
		st.executeUpdate(sql);
		
		JOptionPane.showMessageDialog(null, entityName + " created successfully!");
		listener.infoChanged();
	}
}
