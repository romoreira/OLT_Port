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
            readUntil("User name:","credenciais");
            write(user);
            readUntil("User password:","credenciais");
            write(password);

            // Advance to a prompt
            readUntil(">","inicio");
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage());
        }
    }

//    public void su(String password) {
//        try {
//            write("su");
//            readUntil("Password: ");
//            write(password);
//            prompt = "#";
//            readUntil(prompt + " ");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public String readUntil(String pattern, String comando) {
    	//No primeiro login é necessário ignorar a mensagem do Telent e pegar o segundo > que aparece
    	if(comando.equals("inicio")){
    		try {
    			char lastChar = pattern.charAt(pattern.length() - 1);
    			StringBuffer sb = new StringBuffer();
    			int qtdVezesMaior = 0;
    			char ch = (char) in.read();
    			while (true) {
    				System.out.print(ch);
    				sb.append(ch);
    				if (ch == lastChar) {
    					qtdVezesMaior++;
    					if (sb.toString().endsWith(pattern) && qtdVezesMaior == 2) {
    						return sb.toString();
    					}
    				}
    				ch = (char) in.read();
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	else{
    		if(comando.equals("credenciais")){
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
    		}
    		else{
    			if(comando.equals("scroll 512")){
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
    			}
    			else{
    				if(comando.contains("display board")){
//    					try {
//            				char lastChar = pattern.charAt(pattern.length() - 1);
//            				StringBuffer sb = new StringBuffer();
//            				char ch = (char) in.read();
//            				while (true) {
//            					System.out.print(ch);
//            					sb.append(ch);
//            					if (ch == lastChar) {
//            						if (sb.toString().endsWith(pattern)) {
//            							return sb.toString();
//            						}
//            					}
//            					ch = (char) in.read();
//            				}
//            			} catch (Exception e) {
//            				e.printStackTrace();
//            			}
    				}
    			}
    		}
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
            return readUntil(">", command);
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
            telnet.sendCommand("display board 0/0");
            telnet.disconnect();
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
