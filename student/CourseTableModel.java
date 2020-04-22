package student;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class CourseTableModel extends AbstractTableModel {
	
	private String[] colNames = {"Course_ID", "Name", "Deparment", "Instructor", "Enrolled"};
	private ArrayList<Course> courses = new ArrayList<Course>();
	private boolean checkable = false;
	
	public void setCheckable(boolean b) {
		checkable = b;
	}
	
	public void clear() {
		courses.clear();
		this.fireTableDataChanged();
	}
	
	public List<Course> getCourses(){
		return courses;
	}
	
	public void addCourse(Course c) {
		courses.add(c);
		this.fireTableDataChanged();
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		return getColumnName(column) == "Enrolled" ? Boolean.class : String.class;
	}
	
	public void addRecord(String[] record, boolean selected) {
		courses.add(new Course(record[0], record[1], record[2], record[3], selected));
		this.fireTableDataChanged();
	}
	
	public void setSelected(String courseID, boolean selected) {
		for(Course c : courses)
			if(c.id.equals(courseID))
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
		switch(columnIndex){
			case 0:
				return courses.get(rowIndex).id;
			case 1:
				return courses.get(rowIndex).name;
			case 2:
				return courses.get(rowIndex).department;
			case 3:
				return courses.get(rowIndex).instructor;
			case 4:
				return courses.get(rowIndex).selected;
		}
		return null;
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
