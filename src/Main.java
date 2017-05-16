import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
	public static void main(String args[]){
		try{
			Process p = Runtime.getRuntime().exec("ssh 172.24.158.210");
			PrintStream out = new PrintStream(p.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			out.println("root");
			while (in.ready()) {
			  String s = in.readLine();
			  System.out.println(s);
			}
			out.println("admin");

			while (in.ready()) {
				String s = in.readLine();
				System.out.println(s);
			}
			
			p.waitFor();
		}catch(Exception e){
			System.out.println("Erro: "+e);
		}
	}
}
