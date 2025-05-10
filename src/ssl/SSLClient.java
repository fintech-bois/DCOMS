package ssl;

import javax.net.ssl.*;
import java.io.*;

public class SSLClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8443;

        try {
            System.setProperty("javax.net.ssl.trustStore", "C:\\Program Files\\Java\\jdk1.8.0_111\\bin\\serverkeystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "password");

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            try (
                SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
            ) {
                // User input from console
                System.out.print("Enter username: ");
                String username = consoleReader.readLine();

                System.out.print("Enter password: ");
                String password = consoleReader.readLine();

                // Send to server securely
                writer.println(username + ":" + password);

                // Read server response
                System.out.println("Server response: " + reader.readLine());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
