package sqlexecutor.DataTable;

import sqlexecutor.DataBaseConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

public class ResultTable {

    private DataBaseConnector dataBaseConnector;

    private JPanel contentPane;
    private JTable table;
    private TableModel tableModel;
    private JLabel statusLab;
    private JButton refreshBtn;

    private String lastQuery;

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

        Box topBox = Box.createHorizontalBox();
        refreshBtn = new JButton("Обновить");
        topBox.add(refreshBtn);
        topBox.add(Box.createHorizontalGlue());
        contentPane.add(topBox, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        Box bottomBox = Box.createHorizontalBox();
        bottomBox.add(statusLab);
        bottomBox.add(Box.createHorizontalGlue());
        contentPane.add(bottomBox, BorderLayout.SOUTH);

        refreshBtn.addActionListener(refreshBtnListener);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void executeQuery(String query) {
        ResultSet resultSet;
        try {
            resultSet = dataBaseConnector.executeQuery(query);
            tableModel.refresh(resultSet);
            lastQuery = query;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateStatusLab();
    }

    private void updateStatusLab() {
        String status = "<html>";
        status += "Количество строк: " + tableModel.getRowCount();
        status += " Количество столбцов: " + tableModel.getColumnCount();
        statusLab.setText(status);
    }

    private ActionListener refreshBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (lastQuery == null) return;
            executeQuery(lastQuery);
        }
    };

}
