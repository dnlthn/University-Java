import java.io.*;
import java.net.*;

public class EchoClient {
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.err.println("Usuage: java EchoClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		while(true){	

			//SendMode
			try {

				Socket echoSocket = new Socket(hostName, portNumber);
				PrintWriter outSend = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader inSend = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

				String userInput;
				System.out.println("Type Message >>>");
				while ((userInput = stdIn.readLine()) != null){
					
					//POINT A

					//User Input is withing the userInput variable at this point
					//The below function is the variable that is sent to the server
					outSend.println(userInput);

					System.out.println("Waiting to Recieve message ...");
					//The below variable is feed the response from the server
					userInput = inSend.readLine();

					//POINT B

					//At the point the server response is captured within userInput
					System.out.println("\tRecieved "+userInput);

					System.out.println("Type Message >>>");
				}
			} catch (UnknownHostException e){
				System.err.println("Don't know about host "+hostName);
				System.exit(1);
			} catch (IOException e){
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);
			}
		}
	}
}
