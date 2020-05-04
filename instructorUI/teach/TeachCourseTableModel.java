package instructorUI.teach;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TeachCourseTableModel extends AbstractTableModel {

	private String[] colNames = {"Course ID", "Name", "Deparment", "Instructor"};
	private List<String[]> courses = new ArrayList<String[]>();
	
	public void addRecord(String courseID, String name, String department, String instructor) {
		courses.add(new String[] {courseID, name, department, instructor});
		this.fireTableDataChanged();
	}
	
	public void clear() {
		courses.clear();
		this.fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getRowCount() {
		return courses.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return courses.get(rowIndex)[columnIndex];
	}

}
