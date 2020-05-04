package studentUI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class EnrollTableModel extends AbstractTableModel {
	
	private String[] colNames = {"Course ID", "Name", "Deparment", "Instructor", "Enrolled"};
	private List<CourseRecord> courses = new ArrayList<CourseRecord>();
	private boolean checkable = false;
	
	public void setCheckable(boolean b) {
		checkable = b;
	}
	
	public void clear() {
		courses.clear();
		this.fireTableDataChanged();
	}
	
	public List<CourseRecord> getCourses(){
		return courses;
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		return getColumnName(column) == "Enrolled" ? Boolean.class : String.class;
	}
	
	public void addCourse(CourseRecord c) {
		courses.add(c);
	}
	
	public void addRecord(String courseID, String name, String department, String instructor, boolean selected) {
		courses.add(new CourseRecord(new String[] {courseID, name, department, instructor}, selected));
	}
	
	public void setSelected(String courseID, boolean selected) {
		for(CourseRecord c : courses)
			if(c.info[0].equals(courseID))
				c.selected = selected;
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
		if(columnIndex == getColumnCount() - 1)
			return courses.get(rowIndex).selected;
		return courses.get(rowIndex).info[columnIndex];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getColumnName(columnIndex) == "Enrolled" && checkable;
    }
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(getColumnName(columnIndex) == "Enrolled")
			courses.get(rowIndex).selected = (boolean) aValue;
		this.fireTableDataChanged();
	}

}

class CourseRecord {
	public String[] info;
	public boolean selected;
	public CourseRecord(String[] info, boolean selected) {
		this.info = info;
		this.selected = selected;
	}
}
