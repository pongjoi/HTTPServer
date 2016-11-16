import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPServer {

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Usage: java HTTPServer <port number>");
			System.exit(1);
		}

		// Get the port to listen on
		int portNumber = Integer.parseInt(args[0]);

		// Create a ServerSocket to listen on that port.
		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();

				// Get input and output streams to talk to the client
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

			// read the HTTP request from the client and write it to
			// console
			System.out.println(".....................HTTP-Anfrage......................");
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() == 0)
					break;
				System.out.println(line);
			}

			// Start sending our reply, using the HTTP 1.1 protocol
			out.print("HTTP/1.1 200 OK\r\n"); // Version & status code
			out.print("Content-Type: text/html\r\n"); // The type of data
			out.print("Connection: close\r\n"); // Will close stream
			out.print("\r\n"); // End of headers
			
			// print contents of the page
			String fileName = "example.html";
			for (String fileLine : Files.readAllLines(Paths.get(fileName))) {
				out.print(fileLine + "\r\n");
			}

			// Close socket, breaking the connection to the client, and
			// closing the input and output streams
			out.close(); // Flush and close the output stream
			in.close(); // Close the input stream
			clientSocket.close(); // Close the socket itself

		}

		// If anything goes wrong, print an error message
		catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}
