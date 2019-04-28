package sqlexecutor.DataTable;

import sqlexecutor.DataBaseConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ResultTable {

    private DataBaseConnector dataBaseConnector;

    private JPanel contentPane;
    private JTable table;
    private TableModel tableModel;
    private JLabel statusLab;
    private JButton refreshBtn;

    public ResultTable(DataBaseConnector dataBaseConnector) {
        this.dataBaseConnector = dataBaseConnector;

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        tableModel = new TableModel();
        table = new JTable(tableModel);
        table.setDefaultRenderer(String.class, new TableCellRenderer());
        table.setDefaultRenderer(Date.class, new TableCellRenderer());
        table.setDefaultRenderer(Number.class, new TableCellRenderer());
        table.getTableHeader().setDefaultRenderer(new TableHeaderRenderer());
        statusLab = new JLabel();

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        Box bottomBox = Box.createHorizontalBox();
        bottomBox.add(statusLab);
        bottomBox.add(Box.createHorizontalGlue());
        contentPane.add(bottomBox, BorderLayout.SOUTH);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void setContent(ResultSet resultSet) throws SQLException {
        tableModel.refresh(resultSet);
        updateStatusLab();
    }

    private void updateStatusLab() {
        String status = "<html>";
        status += "Количество строк: " + tableModel.getRowCount();
        status += " Количество столбцов: " + tableModel.getColumnCount();
        statusLab.setText(status);
    }


}
