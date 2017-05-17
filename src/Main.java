import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

public class Main {
	
	private TelnetClient telnet = new TelnetClient();
    private InputStream in;
    private PrintStream out;
    private String prompt = "%";

    public Main(){}
    
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
    					try {
            				char lastChar = pattern.charAt(pattern.length() - 1);
            				StringBuffer sb = new StringBuffer();
            				char ch = (char) in.read();
            				while (true) {
            					System.out.print(ch);
            					sb.append(ch);
            					if ((ch == lastChar)) {
            						if (sb.toString().endsWith(pattern)) {
            							return sb.toString();
            						}
               					}
            					else{
            						if(sb.toString().contains("break")){
            							try {
            								if(sb.toString().contains("Unknown")){
            									System.out.println("Finalizou: "+comando);
            									return sb.toString();
            								}
            								else{
            									sb.append(sendCommand(comando));
            								}
            					        } catch (Exception e) {
            					            System.out.println("Erro: "+e.getMessage());
            					        }
            						}
            					}
            					ch = (char) in.read();
            				}
            			} catch (Exception e) {
            				e.printStackTrace();
            			}
    				}
    			}
    		}
    	}
    	return null;
    }
    
    public void flush(){
    	out.flush();
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
            System.out.println("Executando o comando: "+command);
            Thread.sleep(10000);
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
	
    public void criaArquivoTemporario(String saidaTelnet, String nomeOLT, String sfp){
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File(nomeOLT+"_SFP"+sfp+"_huawei_concessao.tmp"));
			arquivo.write(saidaTelnet);
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Erro ao criar arquivo temporario!"+e.getMessage());
		}
    }
    
    public String trataArquivoTemporario(String nomeOLT, String sfp){
    	try{
    		FileReader fileReader = new FileReader(nomeOLT+"_SFP"+sfp+"_huawei_concessao.tmp");
    			BufferedReader reader = new BufferedReader(fileReader);
    			String linha = null;
    			StringBuffer sb = new StringBuffer();
    			while((linha = reader.readLine()) != null){
    				if(linha.contains("In port")){
    					sb.append(linha).append("\n");
    				}
    			}
    			fileReader.close();
    			reader.close();
    			return sb.toString();
    	}
    	catch(Exception e){
    		System.out.println("Erro ao tratar arquivo temporário - talvez ele não existe!\n"+ e.getMessage());
    	}
		return "Erro - tratamento de arquivo";
    }
    
    public void criaArquivoFinal(String saidaTelnetFinal, String nomeOLT, String sfp){
    	FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File(nomeOLT+"_SFP"+sfp+"_huawei_concessao.txt"));
			arquivo.write(saidaTelnetFinal);
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Erro ao criar arquivo temporario!"+e.getMessage());
		}
    }
    
    public static void registraLOG(String msg){
    	FileWriter arquivo;
    	Date data = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    	BufferedWriter conexao = null;
    	try{
    		FileWriter fw = new FileWriter("LOG_OLT_Port_huawei_concessao.txt", true);
    		conexao = new BufferedWriter(fw);
    	}
    	catch(Exception e){
    		System.out.println("Abrindo arquivo existente");
    	}
		
		try {
			conexao.newLine();
			conexao.write("Leitura de porta de OLT concluida - "+msg+" - " + df.format(data));
			conexao.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Erro ao criar arquivo temporario!"+e.getMessage());
		}
    }
    
	public static void main(String args[]){
	
//		for (int i = 0; i < args.length; i++){
//            if(args[i] == null){
//            	System.out.println("Erro com os parametros");
//            }
//		}

		String nomeArquivoOLTs = "olts_concessao_huawei";
		ArrayList<Elemento> listaOLTs = new ArrayList<Elemento>();
		
		
		try{
    		FileReader fileReader = new FileReader(nomeArquivoOLTs+".txt");
    			BufferedReader reader = new BufferedReader(fileReader);
    			String linha = null;
    			StringBuffer sb = new StringBuffer();
    			while((linha = reader.readLine()) != null){
    				
    				Elemento olt = new Elemento();
    				
    				String nomeOLT = linha.substring(0, linha.indexOf(";"));
    				//System.out.println("Nome OLT: "+nomeOLT);
    				olt.setNomeOLT(nomeOLT);
    				
    				String temporaria = linha.substring(linha.indexOf(";") + 1,linha.length());
    				//System.out.println("String temporaria: "+temporaria);
    				String ipOLT = temporaria.substring(0, temporaria.indexOf(";"));
    				olt.setIpOLT(ipOLT);
    				//System.out.println("IP da OLT: "+ipOLT);
    				
    				olt.setQtdSFP(temporaria.substring(temporaria.indexOf(";")+1));
    				
    				listaOLTs.add(olt);
    				
    			}
    			fileReader.close();
    			reader.close();
    	}
    	catch(Exception e){
    		System.out.println("Erro ao ler arquivo (Parametro Inicial)"+e.getMessage());
    	}
		
		for (int i = 0; i < listaOLTs.size(); i++) {
			int qtdSFP = 0;
			qtdSFP = Integer.parseInt(listaOLTs.get(i).getQtdSFP());
			for (int j = 0; j < qtdSFP; j++) {
				try {
					Main telnet = new Main(listaOLTs.get(i).getIpOLT(), "root", "admin");
					telnet.sendCommand("scroll 512");
					telnet.criaArquivoTemporario(telnet.sendCommand("display board 0/" + j),listaOLTs.get(i).getNomeOLT(), String.valueOf(j));
					telnet.criaArquivoFinal(telnet.trataArquivoTemporario(listaOLTs.get(i).getNomeOLT(), String.valueOf(j)),listaOLTs.get(i).getNomeOLT(), String.valueOf(j));
					telnet.disconnect();
					System.out.println("DONE");
					Thread.sleep(10000);
				} catch (Exception e) {
					System.out.println("Erro no loop principal");
					e.printStackTrace();
				}
			}
		}
		registraLOG("");
	}
}
