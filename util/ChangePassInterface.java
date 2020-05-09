package util;

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
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class ChangePassInterface extends JFrame {
	
	Connection connection;
	String infoID;
	JPasswordField curField, newField, confirmField;
	
	public ChangePassInterface(Connection connection, String infoID) {
		super("Change Password");
		this.connection = connection;
		this.infoID = infoID;
		this.setLayout(new GridBagLayout());
		this.setSize(400, 300);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 10;
		c.weighty = 10;
		
		JLabel curLabel = new JLabel("Current Password");
		c.gridx = 0;
		c.gridy = 0;
		this.add(curLabel, c);
		
		curField = new JPasswordField(20);
		c.gridx = 1;
		this.add(curField, c);
		
		JLabel newLabel = new JLabel("New Password");
		c.gridx = 0;
		c.gridy = 1;
		this.add(newLabel, c);
		
		newField = new JPasswordField(20);
		c.gridx = 1;
		this.add(newField, c);
		
		JLabel confirmLabel = new JLabel("Confirm New Password");
		c.gridx = 0;
		c.gridy = 2;
		this.add(confirmLabel, c);
		
		confirmField = new JPasswordField(20);
		c.gridx = 1;
		this.add(confirmField, c);
		
		JButton changeB = new JButton("Change Password");
		changeB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					change();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		this.add(changeB, c);
	}
	
	private void change() throws SQLException {
		Statement st = connection.createStatement();
		String sql = String.format("SELECT login_pass FROM Person.UserInfo WHERE info_id = '%s';", infoID);
		ResultSet result = st.executeQuery(sql);
		result.next();
		
		String curPass = new String(curField.getPassword());
		if(curPass.compareTo(result.getString(1)) != 0) {
			JOptionPane.showMessageDialog(null, "Current password is incorrect!");
			return;
		}
		
		String newPass = new String(newField.getPassword());
		String confirmPass = new String(confirmField.getPassword());
		if(newPass.compareTo(confirmPass) != 0) {
			JOptionPane.showMessageDialog(null, "Confirming password is incorrect!");
			return;
		}
		
		sql = String.format("UPDATE Person.UserInfo SET login_pass = '%s' WHERE info_id = '%s';", newPass, infoID);
		st.executeUpdate(sql);
		
		JOptionPane.showMessageDialog(null, "Password changed!");
		this.dispose();
	}
}
