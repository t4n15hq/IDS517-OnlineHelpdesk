import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color; // Import statement for java.awt.Color
import java.awt.Component;
import javax.swing.JTable;


public class DarkCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(Color.LIGHT_GRAY); // Set the background color to dark gray
        c.setForeground(Color.black); // Set the text color to white
        return c;
    }
}
