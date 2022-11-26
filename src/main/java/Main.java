import com.jcraft.jsch.JSchException;

public class Main {
    public static void main(String[] args) throws JSchException {
        SshHadoop remote = new SshHadoop();
        remote.downloadHadoopInformation();

        LogFileParser logFileParser = new LogFileParser();
        int[] summary = logFileParser.LogFileParsing();
        System.out.println("================================================================================================================================================");

        System.out.println("Error: " + summary[0]);
        System.out.println("Warning: " + summary[1]);
        System.out.println("Info: " + summary[2]);
    }
}
