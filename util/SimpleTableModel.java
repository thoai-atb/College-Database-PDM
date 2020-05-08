package util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SimpleTableModel extends AbstractTableModel {
	
	public SimpleTableModel(String[] columnNames) {
		colNames = columnNames;
	}
	
	String[] colNames;
	List<String[]> data = new ArrayList<String[]>();
	
	public void clear() {
		data.clear();
		this.fireTableDataChanged();
	}
	
	public void addRecord(String[] record) {
		data.add(record);
		this.fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex)[columnIndex];
	}

}
