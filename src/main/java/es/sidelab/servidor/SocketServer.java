package es.sidelab.servidor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketServer {
	private static int puerto=6969;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{
		ServerSocket serverSocker= new ServerSocket(puerto);
		Socket socket=serverSocker.accept();
		BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw=new PrintWriter(socket.getOutputStream());
		System.out.println("Server iniciado");
		
		while(true){
			String line=br.readLine();
			pw.println(line);
			pw.flush();
			
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}

	}

}
