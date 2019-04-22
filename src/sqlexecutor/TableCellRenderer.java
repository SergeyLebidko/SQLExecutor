package sqlexecutor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {

    private Color evenCellsColor = new Color(230,230,230);
    private Color notEvenCellsColor = new Color(255,255,255);

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        JLabel lab = (JLabel) (super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column));
        lab.setHorizontalAlignment(SwingConstants.LEFT);
        if (!isSelected){
            if ((row%2)==0){
                lab.setBackground(evenCellsColor);
            }
            if ((row%2)!=0){
                lab.setBackground(notEvenCellsColor);
            }
        }
        return lab;
    }

}
