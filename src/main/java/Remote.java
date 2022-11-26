@Data
public class Remote {
    private String user = "admin";
    private String host = "";
    private int port = 22;
    private String password = "adb@2015";
    private String identity = "~/.ssh/id_rsa";
    private String passphrase = "";


    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getIdentity() {
        return identity;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setHost(String s) {
        this.host = s;
    }
}
