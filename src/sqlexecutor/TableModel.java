package sqlexecutor;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel {

    private ResultSet resultSet;

    private ArrayList<ArrayList<Object>> m;

    public TableModel() {
        m = new ArrayList<>();
    }

    public void refresh(ResultSet rs) throws SQLException {
        resultSet = rs;
        m.clear();
        ArrayList<Object> row;
        while (rs.next()) {
            row = new ArrayList<>();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(rs.getObject(i));
            }
            m.add(row);
        }
        fireTableStructureChanged();
    }

    public int getRowCount() {
        if (resultSet == null) return 0;
        return m.size();
    }

    public int getColumnCount() {
        if (resultSet == null) return 0;

        ResultSetMetaData metaData = null;
        int columnCount = 0;
        try {
            metaData = resultSet.getMetaData();
            columnCount = metaData.getColumnCount();
        } catch (SQLException e) {
        }
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

        Object result = null;
        result = m.get(rowIndex).get(columnIndex);
        if (result == null) result = "null";

        return result;
    }

}
