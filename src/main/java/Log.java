import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Log {
    private String ID;
    private String date;
    private String time;
    private String level;
    private String requester;
    private String request;
    private int errors;
    private int warnings;
    private int info;
    private String details;
    public Log(String ID,String date, String time, String level, String requester, String request,int errors,int warnings,int info,@Nullable String details) {
        this.ID =ID;
        this.date = date;
        this.time = time;
        this.level = level;
        this.requester = requester;
        this.request = request;
        this.errors =errors;
        this.warnings = warnings;
        this.info = info;
        this.details =details;
    }

    @NotNull

    public String getID() {return ID;}

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLevel() {
        return level;
    }

    public String getRequester() {
        return requester;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getErrors() {
        return errors;
    }

    public int getWarnings() {
        return warnings;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }
}