import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyClient {
    public static void main(String[] args) {
        //make sure we are getting the right amount of args
        if (args.length != 3) {
            return;
        }

        //get input from args
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        String path = args[2];

        try {
            //use a socket to connect to server
            Socket clientSocket = new Socket(address, port);

            //create input and output streams
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //construct get request
            String req = "";
            req += "GET " + path + " HTTP/1.1\n";
            req += "Host: " + address + "\n";
            req += "Connection: close\n\n";

            //request to server
            out.writeBytes(req);

            //display the response
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            //close streams and sockets
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
