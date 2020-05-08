package instructorUI.department;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.InfoListener;

@SuppressWarnings("serial")
public class AddCourseInterface extends JFrame {
	
	private Connection connection;
	private String departmentID;
	private JTextField idField, nameField, insField, depField;
	private InfoListener listener;
	
	public AddCourseInterface(Connection connection, String departmentID) {
		super("Add Course");
		
		this.connection = connection;
		this.departmentID = departmentID;
			
		this.setVisible(true);
		this.setSize(300, 300);
		this.setLocationRelativeTo(null);
		
		this.setLayout(new BorderLayout());
		JPanel infoPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 10;
		c.weighty = 20;
		
		JLabel depLabel = new JLabel("Department ID");
		c.gridx = 0;
		c.gridy = 0;
		infoPanel.add(depLabel, c);
		
		JLabel idLabel = new JLabel("Course ID");
		c.gridx = 0;
		c.gridy = 1;
		infoPanel.add(idLabel, c);
		
		JLabel nameLabel = new JLabel("Name");
		c.gridx = 0;
		c.gridy = 2;
		infoPanel.add(nameLabel, c);
		
		JLabel insLabel = new JLabel("Instructor ID");
		c.gridx = 0;
		c.gridy = 3;
		infoPanel.add(insLabel, c);
		
		depField = new JTextField(15);
		depField.setText(departmentID);
		depField.setEditable(false);
		c.gridx = 1;
		c.gridy = 0;
		infoPanel.add(depField, c);
		
		idField = new JTextField(15);
		c.gridx = 1;
		c.gridy = 1;
		infoPanel.add(idField, c);
		
		nameField = new JTextField(15);
		c.gridx = 1;
		c.gridy = 2;
		infoPanel.add(nameField, c);
		
		insField = new JTextField(15);
		c.gridx = 1;
		c.gridy = 3;
		infoPanel.add(insField, c);
		
		this.add(infoPanel, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		JButton addB = new JButton("Create Course");
		addB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					addCourse();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		controlPanel.add(addB);
		this.add(controlPanel, BorderLayout.SOUTH);
	}
	
	private void addCourse() throws SQLException {
		Statement st = connection.createStatement();
		String sql = String.format("INSERT INTO College.Course (course_id, name, department_id, instructor_id) VALUES ('%s', '%s', '%s', '%s');", idField.getText(), nameField.getText(), departmentID, insField.getText());
		int i = st.executeUpdate(sql);
		if(i > 0) {
			idField.setText("");
			nameField.setText("");
			insField.setText("");
			JOptionPane.showMessageDialog(null, "Course created successfully!");
			listener.infoChanged();
			this.dispose();
		}
	}
	
	public void setListener(InfoListener listener) {
		this.listener = listener;
	}
}
