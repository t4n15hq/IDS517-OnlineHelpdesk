import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String clientMessage;

            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Message from client: " + clientMessage);
                // Splitting the client message with the assumption it's formatted as "login,email,password"
                String[] tokens = clientMessage.split(",");
                if ("login".equals(tokens[0]) && tokens.length == 3) {
                    boolean loginSuccess = DatabaseHelper.loginUser(tokens[1], tokens[2]);
                    writer.println(loginSuccess ? "success" : "failure");
                } else {
                    writer.println("error:invalid_request"); // Sending an error for invalid requests
                }
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Server thread exception: " + e.getMessage());
        }
    }
}
