package general;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import instructorUI.InstructorInterface;
import studentUI.StudentInterface;

@SuppressWarnings("serial")
public class LoginInterface extends JFrame {
	
	Connection connection;
	JRadioButton studentB, instructorB, staffB;
	JTextField idField = new JTextField(20);
	JPasswordField passField = new JPasswordField(20);
	
	public LoginInterface(Connection connection) throws SQLException {
		super("College Database");
		this.connection = connection;
		
		
		JPanel panel = new JPanel();
		Border outside = BorderFactory.createEmptyBorder(20, 20, 20, 20);
		Border inside = BorderFactory.createTitledBorder("Choose Role");
		panel.setBorder(BorderFactory.createCompoundBorder(outside, inside));
		
		studentB = new JRadioButton("Student");
		instructorB = new JRadioButton("Instructor");
		staffB = new JRadioButton("Staff");

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 20;
		c.weighty = 20;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(studentB, c);
		c.gridx = 1;
		panel.add(instructorB, c);
		c.gridx = 2;
		panel.add(staffB, c);
		
		ButtonGroup group = new ButtonGroup();
		group.add(studentB);
		group.add(instructorB);
		group.add(staffB);
		
		JLabel idLabel = new JLabel("User ID");
		JLabel passLabel = new JLabel("Password");
		c.gridx = 0;
		c.gridy = 1;
		panel.add(idLabel, c);
		c.gridy = 2;
		panel.add(passLabel, c);
		
		passField.setEchoChar('*');
	
		c.fill = GridBagConstraints.NONE; 
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 1;
		panel.add(idField, c);
		c.gridy = 2;
		panel.add(passField, c);
		
		JButton loginB = new JButton("Login");
		loginB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 3;
		panel.add(loginB, c);
		
		this.add(panel);

		this.setSize(500, 500);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void login() {
		String id = idField.getText();
		String pass = new String(passField.getPassword());
		
		try {
			if(studentB.isSelected())
				studentLogin(id, pass);
			else if(instructorB.isSelected())
				instructorLogin(id, pass);
			else if(staffB.isSelected())
				staffLogin(id, pass);
			else
				JOptionPane.showMessageDialog(this, "Please select a role!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private void studentLogin(String id, String pass) throws SQLException  {
		Statement st = connection.createStatement();
		String subSql = "SELECT info_id FROM Person.Student WHERE student_id = '" + id + "'";
		String sql = "SELECT * FROM Person.UserInfo WHERE info_id = (" + subSql + ");";
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			if(pass.equals(result.getString("login_pass"))) {
				new StudentInterface(connection, id);
				st.close();
				dispose();
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "Login failed!");
	}
	
	private void instructorLogin(String id, String pass) throws SQLException {
		Statement st = connection.createStatement();
		String subSql = "SELECT info_id FROM Person.Instructor WHERE instructor_id = '" + id + "'";
		String sql = "SELECT * FROM Person.UserInfo WHERE info_id = (" + subSql + ");";
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			if(pass.equals(result.getString("login_pass"))) {
				new InstructorInterface(connection, id);
				st.close();
				dispose();
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "Login failed!");
	}
	
	private void staffLogin(String id, String pass) throws SQLException {
		Statement st = connection.createStatement();
		String subSql = "SELECT info_id FROM Person.Staff WHERE staff_id = '" + id + "'";
		String sql = "SELECT * FROM Person.UserInfo WHERE info_id = (" + subSql + ");";
		ResultSet result = st.executeQuery(sql);
		while(result.next()) {
			if(pass.equals(result.getString("login_pass"))) {
				new StudentInterface(connection, id);
				st.close();
				dispose();
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "Login failed!");
	}
}


