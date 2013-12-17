package utilities;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
    
	private static final long serialVersionUID = 1L;

	String[] columnNames;
    
    Object[][] data;
    
    public MyTableModel(Object[][] objs, String[] colNames) {
        super();
        data = objs;
        columnNames = colNames;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return data[i][i1];
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
}
