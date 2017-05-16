import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
	public static void main(String args[]){
		try{
			Process p = Runtime.getRuntime().exec("ssh 172.24.149.100 -l admin");
			PrintStream out = new PrintStream(p.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			
			Thread.sleep(4000);
			System.out.println("Antes de exibir pela primeira vez");
			while (in.ready()) {
				String s = in.readLine();
				System.out.println(s);
			}
			out.println("zhone");
			Thread.sleep(4000);
			
			System.out.println("Antes de exibir novamente");
			while (in.ready()) {
				String s = in.readLine();
				System.out.println(s);
			}
			
			System.out.println("Antes do waitfor");
			p.waitFor();
		}catch(Exception e){
			System.out.println("Erro: "+e);
		}
	}
}
