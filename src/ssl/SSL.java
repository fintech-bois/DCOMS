package ssl;

import javax.net.ssl.*;
import java.io.*;
import java.rmi.Naming;
import java.security.KeyStore;
import dcoms.UserService; // Import your RMI interface

public class SSL {

    public static void main(String[] args) {
        int port = 8443;

        try {
            // Load the keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream("C:\\Program Files\\Java\\jdk1.8.0_111\\bin\\serverkeystore.jks")) {
                keyStore.load(fis, "password".toCharArray());
            }

            // Initialize KeyManagerFactory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, "password".toCharArray());

            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            // Create SSL ServerSocket
            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

            System.out.println("SSL Server started on port " + port);

            while (true) {
                try (
                    SSLSocket socket = (SSLSocket) serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
                ) {
                    System.out.println("Client connected.");
                    String received = reader.readLine(); // format: username:password
                    System.out.println("Received: " + received);

                    String[] parts = received.split(":");
                    String username = parts[0];
                    String password = parts[1];

                    try {
                        // Connect to RMI UserService
                        UserService userService = (UserService) Naming.lookup("rmi://localhost/Users");
                        String response = userService.authenticateUser(username, password);

                        // Customize based on return value
                        if (response.equalsIgnoreCase("Invalid")) {
                            writer.println("Invalid credentials.");
                        } else {
                            writer.println("Login successful. UserType: " + response);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        writer.println("RMI error: " + e.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





