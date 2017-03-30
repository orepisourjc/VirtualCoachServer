package es.sidelab.servidor;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SocketServer implements Runnable {

	Socket socket;


	public SocketServer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
			
			String line = is.readLine();
			//os.println(line);
			//os.flush();
			System.out.println(line);
			 
			  
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			// Get a Properties object
			Properties props = System.getProperties();
			props.setProperty("mail.smtp.host", "smtp.gmail.com");
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "true");
			props.put("mail.store.protocol", "pop3");
			props.put("mail.transport.protocol", "smtp");
			final String username = "vitualcoach@gmail.com";
			final String password = "urjc1995";
			try {
				Session session = Session.getDefaultInstance(props, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				// -- Create a new message --
				Message msg = new MimeMessage(session);
				// -- Set the FROM and TO fields --
				msg.setFrom(new InternetAddress(username));
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(line, false));
				msg.setSubject("VirtualCoach");
				msg.setText("Gracias por registrarte en VirtualCoach, dentro de poco podras empezar a registrar marcas y ejercicios.");
				msg.setSentDate(new Date());
				Transport.send(msg);
				System.out.println("Mensaje enviado");
			} catch (MessagingException e) {
				System.out.println("error al enviar mensaje" + e);
			}
		
			
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Fallo en la conexion.");
		}
	}

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(5555);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread t = new Thread(new SocketServer(socket));
				t.start();
			}
		} catch (IOException e) {
			System.out.println("Fallo en la conexion.");
		}

	}
}