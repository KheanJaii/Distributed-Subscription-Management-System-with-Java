import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER1_HOST = "localhost";
    private static final int SERVER1_PORT = 5001;
    private static final String SERVER2_HOST = "localhost";
    private static final int SERVER2_PORT = 5002;
    private static final String SERVER3_HOST = "localhost";
    private static final int SERVER3_PORT = 5003;

    public static void main(String[] args) {
        // Abone olma isteği
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1");

        // Abonelik iptali isteği
        sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2");

        // Giriş isteği
        sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33");

        // Çıkış isteği
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99");

        // Yeniden abone olma isteği
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1");

        // Yeniden abonelik iptali isteği
        sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2");

        // Yeniden giriş isteği
        sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33");

        // Yeniden çıkış isteği
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99");
    }

    private static void sendAndReceiveMessage(String host, int port, String message) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Sunucuya bir mesaj gönder
            System.out.println("Sending message to Server on port " + port + ": " + message);
            out.println(message);

            // Sunucudan gelen yanıtı al
            String response = in.readLine();
            System.out.println("Received response from Server on port " + port + ": " + response);

            // Yanıtı kontrol et
            if (response != null) {
                if (response.equals("55 TAMM")) {
                    System.out.println("İşlem başarılı!");
                } else if (response.equals("50 HATA")) {
                    System.out.println("İşlem başarısız. Hata!");
                } else {
                    System.out.println("Bilinmeyen bir yanıt alındı: " + response);
                }
            } else {
                System.out.println("Sunucudan null yanıt alındı.");
            }

        } catch (IOException e) {
            System.out.println("Error connecting to server on port " + port + ": " + e.getMessage());
        }
    }

}
