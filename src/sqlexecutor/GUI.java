package sqlexecutor;

import sqlexecutor.DataTable.ResultTable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private JButton insertRowBtn;
    private JComboBox<String> tablesList;

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
            executeQueryBtn = new JButton("Выполнить запрос на получение данных");
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

            insertRowBtn = new JButton("Добавить строку в таблицу");
            tablesList = new JComboBox<>(new String[]{"Покупатели", "Офисы", "Заказы", "Товары", "Служащие"});
            toolBar.add(insertRowBtn);
            toolBar.add(Box.createHorizontalStrut(1));
            toolBar.add(tablesList);

            toolBar.add(Box.createHorizontalStrut(800));

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

        insertRowBtn.addActionListener(insertRowBtnListener);

        frm.setContentPane(contentPane);
        frm.setVisible(true);
    }

    private ActionListener executeQueryBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainTable.executeQuery(sqlQueryArea.getText());
        }
    };

    private ActionListener insertRowBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tableName = (String) tablesList.getSelectedItem();
            String queryTemplate = "";
            String[] valNames = null;
            switch (tableName) {
                case "Покупатели": {
                    queryTemplate="INSERT INTO CUSTOMERS VALUES (%s , '%s' , %s , %s )";
                    valNames = new String[]{"cust_num (Integer)", "company (String)", "cust_rep (Integer)", "credit_limit (BigDecimal)"};
                    break;
                }
                case "Офисы": {
                    queryTemplate="INSERT INTO OFFICES VALUES (%s , '%s' , '%s' , %s , %s , %s)";
                    valNames = new String[]{"office (Integer)", "city (String)", "region (String)", "mgr (Integer)", "target (BigDecimal)", "sales (BigDecimal)"};
                    break;
                }
                case "Заказы": {
                    queryTemplate="INSERT INTO ORDERS VALUES (%s , '%s' , %s , %s , '%s' , '%s' , %s , %s)";
                    valNames = new String[]{"order_num (Integer)", "order_date (Date)", "cust (Integer)", "rep (Integer)", "mfr (String)", "product (String)", "qty (Integer)", "amount (BigDecimal)"};
                    break;
                }
                case "Товары": {
                    queryTemplate="INSERT INTO PRODUCTS VALUES ('%s' , '%s' , '%s' , %s , %s)";
                    valNames = new String[]{"mfr_id (String)", "product_id (String)", "description (String)", "price (BigDecimal)", "qty_on_hand (Integer)"};
                    break;
                }
                case "Служащие": {
                    queryTemplate="INSERT INTO PRODUCTS VALUES (%s , '%s' , %s , %s , '%s' , '%s' , %s , %s , %s)";
                    valNames = new String[]{"empl_num (Integer)", "name (String)", "age (Integer)", "rep_office (Integer)", "title (String)", "hire_date (Date)", "manager (Integer)", "quota (BigDecimal)", "sales (BigDecimal)"};
                }
            }

            //Выводим запрос
            String[] vals = showInputValuesDialog(valNames);

            //Если все поля пусты, то просто выходим без выполнения запроса
            boolean found=false;
            for (String s: vals){
                if (!s.equals("")){
                    found=true;
                    break;
                }
            }
            if (!found)return;

            //Проигнорированные пользователем поля считаем равными NULL
            for (int i=0;i<vals.length;i++){
                if (vals[i].equals("")){
                    vals[i]="NULL";
                }
            }

            //Формируем SQL-код запроса
            String query = String.format(queryTemplate, vals);

            //Выполняем запрос
            try {
                dataBaseConnector.updateQuery(query);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frm, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    };

    private ActionListener showBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(frm, false);
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

            dialog.setTitle(title);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setSize(new Dimension(WIDTH_DIALOG, HEIGHT_DIALOG));
            int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH_DIALOG / 2;
            int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT_DIALOG / 2;
            dialog.setLocation(xPos, yPos);

            JPanel dialogPane = new JPanel();
            dialogPane.setLayout(new BorderLayout(5, 5));
            dialogPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            ResultTable dialogTable = new ResultTable(dataBaseConnector);
            dialogTable.executeQuery(query);
            dialogPane.add(dialogTable.getContentPane(), BorderLayout.CENTER);

            dialog.add(dialogPane, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    };

    private String[] showInputValuesDialog(String[] valNames) {
        int valCount = valNames.length;
        String[] result = new String[valCount];
        JTextField[] valFields = new JTextField[valCount];

        JPanel dialogPane = new JPanel();
        dialogPane.setLayout(new GridLayout(0, 2, 5, 5));
        dialogPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < valCount; i++) {
            dialogPane.add(new JLabel(valNames[i] + "        "));
            valFields[i] = new JTextField("");
            dialogPane.add(valFields[i]);
        }

        JOptionPane.showMessageDialog(frm, dialogPane, "", JOptionPane.QUESTION_MESSAGE);

        for (int i = 0; i < valCount; i++) {
            result[i] = valFields[i].getText();
        }

        return result;
    }

}
