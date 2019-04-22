package sqlexecutor.DataTable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableHeaderRenderer extends DefaultTableCellRenderer {

    private final Color backColor = new Color(200, 200, 200);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lab = (JLabel) (super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column));
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        lab.setBackground(backColor);
        lab.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        return lab;
    }

}




