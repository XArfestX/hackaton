import com.jcraft.jsch.JSchException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {
    private JLabel LAbelH;
    private JPanel RootPanel;
    private JTabbedPane tabbedPane1;
    private JTextArea SearchText;
    private JButton UPDATEButton;
    private JButton SEARCHButton;
    private JTable ErrorsInfo;
    private JLabel IPlabel;
    private JLabel Errors;
    private JLabel Warnings;
    private JLabel Info;
    static List<Log> logs = new ArrayList<>();
    LogTableModel logTableModel;

    public MainGUI () throws JSchException {
        add(RootPanel);
        setTitle("Hackaton");
        setSize(1500,1000);

        LogParser logFileParser = new LogParser();
        int WARN = 0;
        int ERROR = 0;
        int INFO = 0;
        SshHadoop remote = new SshHadoop();
        remote.downloadHadoopInformation();

        List <String> files = SshHadoop.paths;
        for(int i = 0; i < files.size(); i++){
            List<Log> temp = logFileParser.LogParsing(files.get(i));
            logs.addAll(temp);
            System.out.println(logs.size());
            WARN += temp.get(temp.size()-1).getWarnings();
            ERROR += temp.get(temp.size()-1).getErrors();
            INFO += temp.get(temp.size()-1).getInfo();

        }


        logTableModel = new LogTableModel(logs);
        ErrorsInfo.setModel(logTableModel);
        IPlabel.setText("All");

        Errors.setText(String.valueOf(ERROR));
        Warnings.setText(String.valueOf(WARN));
        Info.setText(String.valueOf(INFO));
        System.out.println(logTableModel.getRowCount());

        SEARCHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = SearchText.getText();
                logs.clear();
                for(int i = 0; i < files.size(); i++){
                    List <Log> temp = logFileParser.LogParsing(files.get(i),search);
                    logs.addAll(temp);
                }
                logTableModel = new LogTableModel(logs);
                ErrorsInfo.setModel(logTableModel);
                ErrorsInfo.updateUI();
            }
        });
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logs.clear();
                LogParser logFileParser = new LogParser();
                int WARN = 0;
                int ERROR = 0;
                int INFO = 0;
                SshHadoop remote = new SshHadoop();
                try {
                    remote.downloadHadoopInformation();
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }

                List <String> files = SshHadoop.paths;
                for(int i = 0; i < files.size(); i++){
                    List<Log> temp = logFileParser.LogParsing(files.get(i));
                    logs.addAll(temp);
                    System.out.println(logs.size());
                    WARN += temp.get(temp.size()-1).getWarnings();
                    ERROR += temp.get(temp.size()-1).getErrors();
                    INFO += temp.get(temp.size()-1).getInfo();

                }


                logTableModel = new LogTableModel(logs);
                ErrorsInfo.setModel(logTableModel);

                IPlabel.setText("All");

                Errors.setText(String.valueOf(ERROR));
                Warnings.setText(String.valueOf(WARN));
                Info.setText(String.valueOf(INFO));
                System.out.println(logTableModel.getRowCount());
            }
        });
    }

}
