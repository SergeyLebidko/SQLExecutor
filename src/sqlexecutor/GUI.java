package sqlexecutor;

import sqlexecutor.DataTable.ResultTable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class GUI {

    private final int WIDTH_FRM = 1400;
    private final int HEIGHT_FRM = 850;

    private final int WIDTH_DIALOG = 1100;
    private final int HEIGHT_DIALOG = 600;

    private JFrame frm;
    private DataBaseConnector dataBaseConnector;

    ResultTable mainTable;

    private JTextArea sqlQueryArea;
    private JButton executeQueryBtn;

    private JToolBar toolBar;

    private JButton showCustomers;
    private JButton showOffices;
    private JButton showOrders;
    private JButton showProducts;
    private JButton showSalesreps;

    public GUI() {
        frm = new JFrame("SQLExecutor");
        frm.setIconImage(new ImageIcon("res\\logo.png").getImage());
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(WIDTH_FRM, HEIGHT_FRM);
        int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH_FRM / 2;
        int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT_FRM / 2;
        frm.setLocation(xPos, yPos);

        try {
            dataBaseConnector = new DataBaseConnector();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        JPanel contentPane = new JPanel();
        {
            //Формируем поле ввода запроса и кнопку "Выполнить запрос"
            contentPane.setLayout(new BorderLayout(5, 5));
            contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            sqlQueryArea = new JTextArea();
            executeQueryBtn = new JButton("Выполнить");
            sqlQueryArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            JPanel topPane = new JPanel();
            topPane.setLayout(new BorderLayout(5, 5));
            Box box1 = Box.createHorizontalBox();
            box1.add(sqlQueryArea);
            box1.add(Box.createHorizontalStrut(5));
            Box box2 = Box.createVerticalBox();
            box2.add(executeQueryBtn);
            box2.add(Box.createVerticalGlue());
            box1.add(box2);
            topPane.add(box1, BorderLayout.SOUTH);
            contentPane.add(topPane, BorderLayout.NORTH);

            //Формируем панель инструментов
            toolBar = new JToolBar();
            toolBar.setFloatable(false);
            showCustomers = new JButton("Покупатели");
            showOffices = new JButton("Офисы");
            showOrders = new JButton("Заказы");
            showProducts = new JButton("Товары");
            showSalesreps = new JButton("Служащие");

            toolBar.add(showCustomers);
            toolBar.add(Box.createHorizontalStrut(1));
            toolBar.add(showOffices);
            toolBar.add(Box.createHorizontalStrut(1));
            toolBar.add(showOrders);
            toolBar.add(Box.createHorizontalStrut(1));
            toolBar.add(showProducts);
            toolBar.add(Box.createHorizontalStrut(1));
            toolBar.add(showSalesreps);
            toolBar.addSeparator();

            topPane.add(toolBar, BorderLayout.NORTH);

            //Формируем главную таблицу
            mainTable = new ResultTable(dataBaseConnector);
            contentPane.add(mainTable.getContentPane(), BorderLayout.CENTER);
        }

        executeQueryBtn.addActionListener(executeQueryBtnListener);

        showCustomers.addActionListener(showBtnListener);
        showOffices.addActionListener(showBtnListener);
        showOrders.addActionListener(showBtnListener);
        showProducts.addActionListener(showBtnListener);
        showSalesreps.addActionListener(showBtnListener);

        frm.setContentPane(contentPane);
        frm.setVisible(true);
    }

    private ActionListener executeQueryBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query = sqlQueryArea.getText();
            query = query.trim();
            if (query.equals("")) return;

            String firtsWord = query.split(" ")[0];
            firtsWord = firtsWord.toLowerCase();

            ResultSet resultSet;
            try {
                if (firtsWord.equals("select")){
                    resultSet = dataBaseConnector.executeQuery(query);
                    mainTable.setContent(resultSet);
                    return;
                }
                int rowsCount = dataBaseConnector.updateQuery(query);
                JOptionPane.showMessageDialog(frm, "Запрос выполнен. Затронуто строк: "+rowsCount, "", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frm, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    };

    private ActionListener showBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = "";
            String query = "";
            switch (((JButton) e.getSource()).getText()) {
                case "Покупатели": {
                    title = "Таблица Покупатели (CUSTOMERS)";
                    query = "SELECT * FROM CUSTOMERS";
                    break;
                }
                case "Офисы": {
                    title = "Таблица Офисы (OFFICES)";
                    query = "SELECT * FROM OFFICES";
                    break;
                }
                case "Заказы": {
                    title = "Таблица Заказы (ORDERS)";
                    query = "SELECT * FROM ORDERS";
                    break;
                }
                case "Товары": {
                    title = "Таблица Товары (PRODUCTS)";
                    query = "SELECT * FROM PRODUCTS";
                    break;
                }
                case "Служащие": {
                    title = "Таблица Служащие (SALESREPS)";
                    query = "SELECT * FROM SALESREPS";
                }
            }

            ResultTable dialogTable = new ResultTable(dataBaseConnector);
            try {
                ResultSet resultSet = dataBaseConnector.executeQuery(query);
                dialogTable.setContent(resultSet);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frm, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog(frm, false);
            dialog.setTitle(title);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setSize(new Dimension(WIDTH_DIALOG, HEIGHT_DIALOG));
            int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH_DIALOG / 2;
            int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT_DIALOG / 2;
            dialog.setLocation(xPos, yPos);

            JPanel dialogPane = new JPanel();
            dialogPane.setLayout(new BorderLayout(5, 5));
            dialogPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            dialogPane.add(dialogTable.getContentPane(), BorderLayout.CENTER);
            dialog.add(dialogPane, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    };

}
