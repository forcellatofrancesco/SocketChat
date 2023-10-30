import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Thread listener;
    private final String name;
    private Socket server;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String name) {
        this.name = name;
    }

    private boolean isValidMessage(String message){
        return message != null && !message.equals(":q");
    }

    public void start(final String ipAddress, final int port) throws UnknownHostException, IOException {
        server = new Socket(ipAddress, port);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream());
        listener = new Thread(() -> {
            String message = "";
            while (isValidMessage(message) && !listener.isInterrupted()) {
                try {
                    message = in.readLine();
                    if (isValidMessage(message)) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        listener.start();
    }

    public void send(String message) {
        if (server == null) {
            throw new RuntimeException("Client not started!");
        }
        out.println("[" + name + "]:" + message);
        out.flush();
    }

    public void close() throws IOException, InterruptedException {
        in.close();
        out.close();
        server.close();
    }
}
