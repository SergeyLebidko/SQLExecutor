package sqlexecutor;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel {

    private ResultSet resultSet;
    private Object[][] content;
    private int rowCount, columnCount;

    public TableModel() {
        content= new Object[0][0];
        rowCount = 0;
        columnCount = 0;
    }

    public void refresh(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<Object>> m = new ArrayList<>();
        resultSet = rs;
        m.clear();
        ArrayList<Object> row;
        rowCount=0;
        columnCount = resultSet.getMetaData().getColumnCount();
        while (rs.next()) {
            row = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            m.add(row);
            rowCount++;
        }

        content=new Object[rowCount][columnCount];
        for (int i=0; i<rowCount;i++){
            for (int j=0;j<columnCount;j++){
                content[i][j]=m.get(i).get(j);
            }
        }

        fireTableStructureChanged();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int column) {
        if (resultSet == null) return "";

        ResultSetMetaData metaData = null;
        String columnName = "";
        try {
            metaData = resultSet.getMetaData();
            columnName = "<html><p align=\"center\">" + metaData.getColumnName(column + 1) + "<br>" + metaData.getColumnClassName(column + 1) + "</p>";
        } catch (SQLException e) {
        }
        return columnName;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (resultSet == null) return Object.class;
        return Object.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (resultSet == null) return "";
        return content[rowIndex][columnIndex]==null?"null":content[rowIndex][columnIndex];
    }

}
