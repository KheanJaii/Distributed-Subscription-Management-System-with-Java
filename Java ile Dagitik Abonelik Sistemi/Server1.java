import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server1 {
    private static final int PORT = 5001; // Server1's port
    private static Aboneler aboneler = new Aboneler(); // Aboneler sınıfından bir nesne
    private static final ExecutorService clientHandlerPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server1 is running on port " + PORT);

        new PingThread("localhost", 5002).start(); // Ping Server2
        new PingThread("localhost", 5003).start(); // Ping Server3

        // Listen for client connections
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientHandlerPool.execute(new ClientHandler(clientSocket));
            }
        } finally {
            serverSocket.close();
            clientHandlerPool.shutdown();
        }
    }

    // Thread to handle client requests
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            // Implement client handling logic here
            BufferedReader in = null;
            PrintWriter out = null;
            String message = null; // Tanımlamayı buraya taşıdık

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the message from the client
                message = in.readLine();

                // Check if the message is null
                if (message == null) {
                    System.out.println("Received null message from client.");
                    return; // Exit the method if the message is null
                }

                ASUPProtokol asupProtokol = new ASUPProtokol();

                // Process the client's request and send a response
                String response = processMessage(message, asupProtokol);
                out.println(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Received message on Server1(" + Thread.currentThread().getId() +
                    ") from client: " + message);
        }

        private String processMessage(String message, ASUPProtokol asupProtokol) {
            if (message == null || message.trim().isEmpty()) {
                return asupProtokol.errorMessage("Boş veya geçersiz mesaj.");
            }

            // Handle the received message based on the ASUP protocol
            // Use ASUPProtokol methods to create responses or perform actions
            // ...

            // Example: Assuming ABONOL message format is "ABONOL <index>"
            if (message.startsWith("ABONOL")) {
                String[] parts = message.split(" ");
                if (parts.length == 2) {
                    try {
                        int index = Integer.parseInt(parts[1]);
                        // Check if index is within bounds
                        if (index >= 0 && index < aboneler.getAboneler().size()) {
                            // Example: Add subscriber to index
                            aboneler.aboneEkle(index);
                            // Notify other servers about the update
                            notifyOtherServers(asupProtokol.abonelerUpdateMessage(aboneler));
                            return asupProtokol.responseMessage(true, "Abone eklendi.");
                        } else {
                            return asupProtokol.errorMessage("Geçersiz abone indeksi.");
                        }
                    } catch (NumberFormatException e) {
                        return asupProtokol.errorMessage("Abone indeksi bir sayı olmalı.");
                    }
                }
            } else if (message.startsWith("ABONPTAL")) {
                // Example: Assuming ABONPTAL message format is "ABONPTAL <index>"
                String[] parts = message.split(" ");
                if (parts.length == 2) {
                    try {
                        int index = Integer.parseInt(parts[1]);
                        // Check if index is within bounds
                        if (index >= 0 && index < aboneler.getAboneler().size()) {
                            // Check if the subscriber is already subscribed
                            if (aboneler.getAboneler().get(index)) {
                                // Example: Cancel subscription for the subscriber at index
                                aboneler.abonelikIptal(index);
                                // Notify other servers about the update
                                notifyOtherServers(asupProtokol.abonelerUpdateMessage(aboneler));
                                return asupProtokol.responseMessage(true, "Abonelik iptal edildi.");
                            } else {
                                return asupProtokol.errorMessage("Zaten abone değil durumu.");
                            }
                        } else {
                            return asupProtokol.errorMessage("Geçersiz abone indeksi.");
                        }
                    } catch (NumberFormatException e) {
                        return asupProtokol.errorMessage("Abone indeksi bir sayı olmalı.");
                    }
                }
            } else if (message.startsWith("GIRIS ISTMC")) {
                // Example: Assuming GIRIS ISTMC message format is "GIRIS ISTMC <index>"
                String[] parts = message.split(" ");
                if (parts.length == 3) {
                    try {
                        int index = Integer.parseInt(parts[2]);
                        // Check if index is within bounds
                        if (index >= 0 && index < aboneler.getAboneler().size()) {
                            // Check if the subscriber is subscribed
                            if (aboneler.getAboneler().get(index)) {
                                // Example: Log in the subscriber at index
                                aboneler.girisYap(index);
                                // Notify other servers about the update
                                notifyOtherServers(asupProtokol.abonelerUpdateMessage(aboneler));
                                return asupProtokol.responseMessage(true, "Giriş yapıldı.");
                            } else {
                                return asupProtokol.errorMessage("Abone değil durumu.");
                            }
                        } else {
                            return asupProtokol.errorMessage("Geçersiz abone indeksi.");
                        }
                    } catch (NumberFormatException e) {
                        return asupProtokol.errorMessage("Abone indeksi bir sayı olmalı.");
                    }
                }
            } else if (message.startsWith("CIKIS ISTMC")) {
                // Example: Assuming CIKIS ISTMC message format is "CIKIS ISTMC <index>"
                String[] parts = message.split(" ");
                if (parts.length == 3) {
                    try {
                        int index = Integer.parseInt(parts[2]);
                        // Check if index is within bounds
                        if (index >= 0 && index < aboneler.getGirisYapanlarListesi().size()) {
                            // Check if the subscriber is logged in
                            if (aboneler.getGirisYapanlarListesi().get(index)) {
                                // Example: Log out the subscriber at index
                                aboneler.cikisYap(index);
                                // Notify other servers about the update
                                notifyOtherServers(asupProtokol.abonelerUpdateMessage(aboneler));
                                return asupProtokol.responseMessage(true, "Çıkış yapıldı.");
                            } else {
                                return asupProtokol.errorMessage("Giriş yapmamış olma durumu.");
                            }
                        } else {
                            return asupProtokol.errorMessage("Geçersiz abone indeksi.");
                        }
                    } catch (NumberFormatException e) {
                        return asupProtokol.errorMessage("Abone indeksi bir sayı olmalı.");
                    }
                }
            }

            // Handle other types of messages if needed

            return asupProtokol.errorMessage("Geçersiz istek.");
        }
    }

    // Thread to ping another server
    private static class PingThread extends Thread {
        private String host;
        private int port;

        public PingThread(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // Ping the specified host and port
                    ping(host, port);
                    // Wait for 5 seconds before the next ping
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void ping(String host, int port) {
            try (Socket socket = new Socket()) {
                // Connect to the specified host and port
                socket.connect(new InetSocketAddress(host, port), 1000);
                System.out.println("Pinged " + host + " on port " + port);
            } catch (IOException e) {
                System.out.println("Ping to " + host + " on port " + port + " failed, retrying...");
            }
        }
    }

    // Method to notify other servers about the update
    private static void notifyOtherServers(String message) {
        // Implement logic to send the message to other servers
        // ...
        System.out.println("Notifying other servers: " + message);
    }
}
