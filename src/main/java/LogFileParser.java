import java.util.ArrayList;
import java.util.List;

public class LogFileParser {

    public void LogFileParsing(){
        List<String> files = SshHadoop.paths;
        List<Log> parsed = new ArrayList<>();
        int[] summary = new int[0];
        int summaryError = 0;
        int summaryWarning = 0;
        int summaryInfo = 0;

        for(int i = 0; i < files.size(); i++){
            LogParser logParser = new LogParser();
            System.out.println("================================================================================================================================================");
            System.out.println("Host: " + files.get(i).replace("ip/", "").replace("/", " | "));
            parsed = logParser.LogParsing(files.get(i));
            summaryError += parsed.get(parsed.size() - 1).getErrors();
            summaryWarning += parsed.get(parsed.size() - 1).getWarnings();
            summaryInfo  += parsed.get(parsed.size() - 1).getInfo();
        }
        summary[0] = summaryError;
        summary[1] = summaryWarning;
        summary[2] = summaryInfo;

    }

}

