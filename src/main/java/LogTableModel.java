import javax.swing.table.AbstractTableModel;
import java.util.List;

public  class LogTableModel extends AbstractTableModel {
    public final String[] columns ={"ID","DATE","TIME","LEVEL","REQUESTER","REQUEST"};
    public List<Log> logs;
    public LogTableModel(List<Log>logs){
        this.logs=logs;
    }
    @Override
    public int getRowCount() {
        return logs.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return switch (columnIndex){
            case 0 -> logs.get(rowIndex).getID();
            case 1 -> logs.get(rowIndex).getDate();
            case 2 -> logs.get(rowIndex).getTime();
            case 3 -> logs.get(rowIndex).getLevel();
            case 4 -> logs.get(rowIndex).getRequester();
            case 5 -> logs.get(rowIndex).getRequest();
            default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
        };
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getValueAt(0,columnIndex)!=null)
            return getValueAt(0,columnIndex).getClass();
        else return Object.class;

    }
}
