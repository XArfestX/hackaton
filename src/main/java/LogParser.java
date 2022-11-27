import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogParser {
    public List<Log> LogParsing(String filepath) {
        List<Log> logs = new ArrayList<>();
        String[] nameSplit = filepath.replace("ip/", "").split("/");
        String serverFolder = nameSplit[0]+" ["+nameSplit[1]+"]";
        try {
            String errorDetails ="";
            boolean headerFlag = true;
            int levelInfoCounter = 0;
            int levelWarnCounter = 0;
            int levelErrCounter = 0;
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

            for(DateValidator validator = new LogValidator(dateFormatter); line != null; line = reader.readLine()) {

                String[] logString = line.split(" ");

                if (validator.isValid(logString[0])) {
                    headerFlag = false;
//                    System.out.println(logString[0]);
//                    System.out.println(logString[1]);
                    switch (logString[2]) {
                        case "INFO" -> ++levelInfoCounter;
                        case "ERROR" -> ++levelErrCounter;
                        case "WARN" -> ++levelWarnCounter;
                    }
//                  System.out.println(logString[2]);
//                  System.out.println(logString[3]);
                    String request = "" ;
                    for(int i = 4; i < logString.length; ++i) {
                        request +=logString[i] + " ";
//                        System.out.print(logString[i] + " ");
                    }
                    logs.add(new Log(serverFolder,logString[0],logString[1],logString[2],logString[3],request,levelErrCounter,levelWarnCounter,levelInfoCounter,errorDetails));
                } else if (headerFlag) {
                    for(int i = 0; i < logString.length - 1; ++i) {
//                        System.out.print(logString[i] + " ");
                    }
                } else {
                    for(int i = 0; i < logString.length - 1; ++i) {
                        errorDetails = logString[i] + "\n";
                        // System.out.println(errorDetails + "\n" + "\n");
                    }
                }

//                System.out.println("\n");
            }
            System.out.println("Error: " + levelErrCounter + " | " + "Warning: " + levelWarnCounter + " | " + "Info: " + levelInfoCounter);
            reader.close();
        } catch (IOException var14) {
            var14.printStackTrace();
        }


        return logs;
    }
    public List<Log> LogParsing(String filepath, String search) {
        search = search.replace("\n","");
        List<Log> logs = new ArrayList<>();
        String[] nameSplit = filepath.replace("ip/", "").split("/");
        String serverFolder = nameSplit[0]+" ["+nameSplit[1]+"]";
        try {
            String errorDetails ="";
            boolean headerFlag = true;
            int levelInfoCounter = 0;
            int levelWarnCounter = 0;
            int levelErrCounter = 0;
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

            for(DateValidator validator = new LogValidator(dateFormatter); line != null; line = reader.readLine()) {

                String[] logString = line.split(" ");

                if (validator.isValid(logString[0])) {
                    headerFlag = false;
                    if(line.contains(search)){
                        System.out.println(logString[0]);
                        System.out.println(logString[1]);
                        System.out.println(logString[2]);
                        System.out.println(logString[3]);
                        String request = "" ;
                        for(int i = 4; i < logString.length; ++i) {
                            request +=logString[i] + " ";
//                        System.out.print(logString[i] + " ");
                        }
                        logs.add(new Log(serverFolder,logString[0],logString[1],logString[2],logString[3],request,levelErrCounter,levelWarnCounter,levelInfoCounter,errorDetails));
                    }

                } else if (headerFlag) {
                    for(int i = 0; i < logString.length - 1; ++i) {
//                        System.out.print(logString[i] + " ");
                    }
                } else {
                    for(int i = 0; i < logString.length - 1; ++i) {
                        errorDetails = logString[i] + "\n";
                        // System.out.println(errorDetails + "\n" + "\n");
                    }
                }

//                System.out.println("\n");
            }
            System.out.println("Error: " + levelErrCounter + " | " + "Warning: " + levelWarnCounter + " | " + "Info: " + levelInfoCounter);
            reader.close();
        } catch (IOException var14) {
            var14.printStackTrace();
        }


        return logs;
    }
}
