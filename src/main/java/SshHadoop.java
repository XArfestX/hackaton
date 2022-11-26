import com.jcraft.jsch.ChannelExec;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class SshHadoop {

    private static final int CONNECT_TIMEOUT = 100000;
    //private static ClassLogger log;
    static List <String> folders;
    static List <String> files;
    static List <String> paths = new ArrayList<>();

    public void downloadHadoopInformation() throws JSchException {
        List <String> servers = downloadIp();
        for(int k = 0; k < servers.size() ; k++){
            Remote remote = new Remote();
            remote.setHost(servers.get(k));
            Session session = getSession(remote);
            session.connect(CONNECT_TIMEOUT);
            if (session.isConnected()) {
                System.out.println("Host ("+remote.getHost()+") connected.");
            }

            folders = remoteExecute(session, "ls -d /var/log/*hadoop*");
            for(int i = 0;i<folders.size();i++){
                String str = new String(folders.get(i).toCharArray(),9,(folders.get(i).length())-9);
                files = remoteExecute(session, "realpath ../../var/log/"+str+"/*.log*");
                new File("ip/"+remote.getHost()+"/"+str).mkdirs();
                for(int j = 0;j < files.size();j++){
                    scpFrom(session,files.get(j),"ip/"+remote.getHost()+"/"+str);
                    if(files.get(j).contains(".log") && !(files.get(j).contains("*.log*")))
                        paths.add("ip/"+remote.getHost()+"/"+new String(files.get(j).toCharArray(),9,(files.get(j).length())-9));

                }
            }
            session.disconnect();
            System.out.println("Session ("+remote.getHost()+") closed.\n");
        }
    }

    public static List<String> downloadIp(){
        List<String> IP = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("sources.txt"))) {
            String line = br.readLine();
            System.out.println("Connecting and downloading logs from server(s):\n");
            while (line != null) {

                System.out.println(line);
                IP.add(line);
                line = br.readLine();
            }
            System.out.println("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return IP;
    }



    public static Session getSession(Remote remote) throws JSchException {
        JSch jSch = new JSch();
        if (Files.exists(Paths.get(remote.getIdentity()))) {
            jSch.addIdentity(remote.getIdentity(), remote.getPassphrase());
        }
        Session session = jSch.getSession(remote.getUser(), remote.getHost(),remote.getPort());
        session.setPassword(remote.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }
    public static List<String> remoteExecute(Session session, String command) throws JSchException {
        System.out.println(">> {"+command+"}");
        List<String> resultLines = new ArrayList<>();
        ChannelExec channel = null;
        try{
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream input = channel.getInputStream();
            channel.connect(CONNECT_TIMEOUT);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine = null;
                while((inputLine = inputReader.readLine()) != null) {
                    System.out.println("   {"+inputLine+"}");
                    resultLines.add(inputLine);

                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        System.out.println("JSch inputStream close error: "+ e);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOcxecption: "+ e);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                    System.out.println("JSch channel disconnect error: "+ e);
                }
            }
        }
        return resultLines;
    }
    public static long scpFrom(Session session, String source, String destination) {
        FileOutputStream fileOutputStream = null;
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("scp -f " + source);
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] buf = new byte[1024];
            //send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            while(true) {
                if (checkAck(in) != 'C') {
                    break;
                }
            }
            //read '644 '
            in.read(buf, 0, 4);
            long fileSize = 0;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    break;
                }
                if (buf[0] == ' ') {
                    break;
                }
                fileSize = fileSize * 10L + (long)(buf[0] - '0');
            }
            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            // read a content of lfile
            if (Files.isDirectory(Paths.get(destination))) {
                fileOutputStream = new FileOutputStream(destination + File.separator +file);
            } else {
                fileOutputStream = new FileOutputStream(destination);
            }
            long sum = 0;
            while (true) {
                int len = in.read(buf, 0 , buf.length);
                if (len <= 0) {
                    break;
                }
                sum += len;
                if (len >= fileSize) {
                    fileOutputStream.write(buf, 0, (int)fileSize);
                    break;
                }
                fileOutputStream.write(buf, 0, len);
                fileSize -= len;
            }
            return sum;
        } catch(JSchException e) {
            System.out.println("scp to catched jsch exception, "+ e);
        } catch(IOException e) {
            System.out.println("scp to catched io exception, "+ e);
        } catch(Exception e) {
            System.out.println("scp to error, "+ e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    System.out.println("File output stream close error, "+ e);
                }
            }
        }
        return -1;
    }
    private static int checkAck(InputStream in) throws IOException {
        int b=in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if(b==0) return b;
        if(b==-1) return b;
        if(b==1 || b==2){
            StringBuffer sb=new StringBuffer();
            int c;
            do {
                c=in.read();
                sb.append((char)c);
            }
            while(c!='\n');
            if(b==1){ // error
                System.out.println(sb.toString());
            }
            if(b==2){ // fatal error
                System.out.println(sb.toString());
            }
        }
        return b;
    }


}

