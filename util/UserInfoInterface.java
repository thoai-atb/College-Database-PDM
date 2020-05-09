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
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class UserInfoInterface extends JFrame {

	private Connection connection;
	private String infoID;
	private InfoListener listener;
	
	private JTextField nameField, phoneField, emailField;
	
	public UserInfoInterface(Connection connection, String info_id, InfoListener listener) {
		super("Edit Information");
		this.connection = connection;
		this.infoID = info_id;
		this.listener = listener;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 10;
		c.weighty = 20;
		JLabel nameLabel = new JLabel("User Name");
		c.gridx = 0;
		c.gridy = 0;
		this.add(nameLabel, c);
		JLabel phoneLabel = new JLabel("Phone Number");
		c.gridx = 2;
		c.gridy = 0;
		this.add(phoneLabel, c);
		JLabel emailLabel = new JLabel("Email");
		c.gridx = 0;
		c.gridy = 1;
		this.add(emailLabel, c);
		
		nameField = new JTextField(15);
		c.gridx = 1;
		c.gridy = 0;
		this.add(nameField, c);
		phoneField = new JTextField(15);
		c.gridx = 3;
		c.gridy = 0;
		this.add(phoneField, c);
		emailField = new JTextField(15);
		c.gridx = 1;
		c.gridy = 1;
		this.add(emailField, c);
		
		JButton changePassB = new JButton("Change Password");
		changePassB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePass();
			}
		});
		c.gridx = 3;
		c.gridy = 1;
		this.add(changePassB, c);
		
		JButton saveB = new JButton("Save");
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		c.gridx = 2;
		c.gridy = 1;
		this.add(saveB, c);
		
		try {
			refresh();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		this.setSize(600, 300);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	public void setListener(InfoListener l) {
		this.listener = l;
	}
	
	private void refresh() throws SQLException {
		Statement st = connection.createStatement();
		String sql = String.format("SELECT * FROM Person.UserInfo WHERE info_id = '%s';", infoID);
		ResultSet result = st.executeQuery(sql);
		result.next();
		nameField.setText(result.getString("name"));
		phoneField.setText(result.getString("phone_number"));
		emailField.setText(result.getString("email"));
	}
	
	private void changePass() {
		new ChangePassInterface(connection, infoID);
	}
	
	private void save() throws SQLException {
		Statement st = connection.createStatement();
		String sql = String.format("UPDATE Person.UserInfo SET name = '%s', phone_number = '%s', email = '%s' "
				+ "WHERE info_id = '%s';", nameField.getText(), phoneField.getText(), emailField.getText(), infoID);
		st.executeUpdate(sql);
		JOptionPane.showMessageDialog(null, "Information Saved!");
		listener.infoChanged();
	}
}
