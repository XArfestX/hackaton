import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class LogParser {

    public int[] LogParsing(String filepath) {
        int[] levelSummary = new int[3];

        try {
            boolean headerFlag = true;
            int levelInfoCounter = 0;
            int levelWarnCounter = 0;
            int levelErrCounter = 0;
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

            for(DateValidator validator = new LogValidator(dateFormatter); line != null; line = reader.readLine()) {
                String[] logString = line.split(" ");
                int i;
                if (validator.isValid(logString[0])) {
                    headerFlag = false;
//                    System.out.println(logString[0]);
//                    System.out.println(logString[1]);
                    switch (logString[2]) {
                        case "INFO":
                            ++levelInfoCounter;
                            break;
                        case "ERROR":
                            ++levelErrCounter;
                            break;
                        case "WARN":
                            ++levelWarnCounter;
                    }

//                    System.out.println(logString[2]);
//                    System.out.println(logString[3]);

                    for(i = 4; i < logString.length; ++i) {
//                        System.out.print(logString[i] + " ");
                    }
                } else if (headerFlag) {
                    for(i = 0; i < logString.length - 1; ++i) {
//                        System.out.print(logString[i] + " ");
                    }
                } else {
//                    System.out.println("novalid");
                }

//                System.out.println("\n");
            }

            levelSummary[0] = levelErrCounter;
            levelSummary[1] = levelWarnCounter;
            levelSummary[2] = levelInfoCounter;
            System.out.println("Error: " + levelErrCounter + " | " + "Warning: " + levelWarnCounter + " | " + "Info: " + levelInfoCounter);
            reader.close();
        } catch (IOException var14) {
            var14.printStackTrace();
        }

        return levelSummary;
    }
}
