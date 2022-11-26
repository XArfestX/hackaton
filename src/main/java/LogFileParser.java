import java.util.List;

public class LogFileParser {

    public int[] LogFileParsing(){
        List<String> files = SshHadoop.paths;
        int[] summary = new int[0];
        int summaryError = 0;
        int summaryWarning = 0;
        int summaryInfo = 0;

        for(int i = 0; i < files.size(); i++){
            LogParser logParser = new LogParser();
            System.out.println("================================================================================================================================================");
            System.out.println("Host: " + files.get(i).replace("ip/", "").replace("/", " | "));
            summary = logParser.LogParsing(files.get(i));
            summaryError += summary[0];
            summaryWarning += summary[1];
            summaryInfo += summary[2];
        }
        summary[0] = summaryError;
        summary[1] = summaryWarning;
        summary[2] = summaryInfo;

        return summary;
    }

}
