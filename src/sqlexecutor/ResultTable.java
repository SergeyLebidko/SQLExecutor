package sqlexecutor;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class ResultTable {

    private DataBaseConnector dataBaseConnector;

    private JPanel contentPane;
    private JTable table;
    private TableModel tableModel;
    private JLabel statusLab;

    public ResultTable(DataBaseConnector dataBaseConnector) {
        this.dataBaseConnector = dataBaseConnector;

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        tableModel = new TableModel();
        table = new JTable(tableModel);
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

    public void executeQuery(String query){
        ResultSet resultSet;
        try {
            resultSet = dataBaseConnector.getResultSet(query);
            tableModel.refresh(resultSet);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateStatusLab();
    }

    private void updateStatusLab(){
        String status="<html>";
        status+="Количество строк: "+ tableModel.getRowCount();
        status+=" Количество столбцов: "+ tableModel.getColumnCount();
        statusLab.setText(status);
    }

}
