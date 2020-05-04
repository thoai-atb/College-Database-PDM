package instructorUI.teach;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class StudentTableModel extends AbstractTableModel {
	
	private String[] colNames = {"Student ID", "Name", "Department", "Phone Number", "Email"};
	
	private List<String[]> students = new ArrayList<String[]>();
	
	public void addRecord(String id, String name, String department, String phone, String email) {
		students.add(new String[] {id, name, department, phone, email});
	}
	
	@Override 
	public String getColumnName(int col){
		return colNames[col];
	}

	@Override
	public int getRowCount() {
		return students.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return students.get(rowIndex)[columnIndex];
	}

}
