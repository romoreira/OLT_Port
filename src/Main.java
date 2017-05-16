import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
	public static void main(String args[]){
		try{
			Socket socket = new Socket("172.30.20.93", 23);
			socket.setKeepAlive(true);
			BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//PrintWriter w = new PrintWriter(socket.getOutputStream(),true);

			int c=0;
			while ((c = r.read()) != -1)
				System.out.print((char)c);

			String command="root"; 
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			pw.print(command+"\r\n");
			//teste
			pw.print("admin"+"\r\n");
			
			while ((c = r.read()) != -1)
				System.out.print((char)c);

			socket.close();
		}catch(Exception e){
			System.out.println("Erro: "+e);
		}
	}
}
