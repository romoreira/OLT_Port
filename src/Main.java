import org.apache.commons.net.telnet.TelnetClient;

import java.io.InputStream;
import java.io.PrintStream;

public class Main {
	
	private TelnetClient telnet = new TelnetClient();
    private InputStream in;
    private PrintStream out;
    private String prompt = "%";

    public Main(String server, String user, String password) {
        try {
            // Connect to the specified server
            telnet.connect(server, 23);

            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());

            // Log the user on
            readUntil("User name:");
            write(user);
            readUntil("User password:");
            write(password);

            // Advance to a prompt
            readUntil(">");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void su(String password) {
        try {
            write("su");
            readUntil("Password: ");
            write(password);
            prompt = "#";
            readUntil(prompt + " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
            	System.out.print(ch);
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String value) {
        try {
            out.println(value);
            out.flush();
            System.out.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(">");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String args[]){
		//a-gn-ol-iai-001-salseiros-01
		//172.24.158.194
		
		//a-gn-ol-lgs-001-centro-01
		//172.24.163.34
		
		try {
            Main telnet = new Main("172.24.158.194", "root", "admin");
            telnet.sendCommand("scroll 512");
            Thread.sleep(4000);
            //telnet.sendCommand("display board 0/0 ");
            telnet.disconnect();
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
