import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer {
    public static void main(String[] args) throws IOException {
        // Set the port number
        int port = 80;
        // Establish the listen socket
        ServerSocket welcomeSocket = new ServerSocket(port);
        // Process HTTP service requests in an infinite loop
        while (true) {
            // Listen for a TCP connection request
            Socket connectionSocket = welcomeSocket.accept();
            // Establish the connection
            System.out.println("Ready to serve...");

            // Get a reference to the socket's input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());

            // Read the HTTP request message
            String[] inString = new String[100];
            int i =0;
            while ((inString[i] = in.readLine()) != null) {
                System.out.println(inString[i]);
                if (inString[i].isEmpty()) {
                    break;
                }
                i++;
            }
            // Extract the filename from the request line
            String reqName;
            boolean fileExists = true;
            String data = "";
            FileInputStream myObj = null;

            reqName = inString[0].substring(5, inString[0].lastIndexOf(" "));

            // Open the requested file
            try {
                myObj = new FileInputStream(reqName);
            } catch (FileNotFoundException e) {
                fileExists = false;
            }
            // Construct the response message
            // Construct the response message
            String stat = null;
            String type = null;

            if (fileExists) {
                stat = "HTTP/1.1 200 OK\n";
                type = "Content-Type: text/html\n";
            } else {
                stat = "HTTP/1.1 404 Not Found\n";
                type = "Content-Type: text/html\n";
                data = "404 Not Found\n";
            }

            //Send one HTTP header line into socket
            out.writeBytes(stat);
            out.writeBytes(type);
            out.writeBytes("\n");

            // Send the content of the requested file to the client
            if (fileExists) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = myObj.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } else {
                // error message
                out.writeBytes(data);
            }

            // Close streams and socket
            in.close();
            out.close();
            connectionSocket.close();
        }
    }
}