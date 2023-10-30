import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private final List<Socket> clients;

    public Server() {
        clients = new ArrayList<>();
    }

    public void start(final int port) throws IOException {
        if (serverSocket == null || serverSocket.isClosed()) {
            serverSocket = new ServerSocket(port);
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                clients.add(client);
                Thread t = new Thread(() -> {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));
                        String input = "";
                        while (input != null && !input.contains(":q")) {
                            input = bufferedReader.readLine();
                            System.out.println(input);
                            for (Socket socket : clients) {
                                if (socket != client) {
                                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                                    printWriter.println(input);
                                    printWriter.flush();
                                }
                            }
                        }
                        System.out.println("hey");
                        PrintWriter printWriter = new PrintWriter(client.getOutputStream());
                        printWriter.println(":q");
                        printWriter.flush();
                        clients.remove(client);
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                t.start();
            }
        } else {
            throw new RuntimeException("The server is already running.");
        }
    }

    public void close() throws IOException, InterruptedException {
        for (Socket client : clients) {
            client.close();
        }
        clients.clear();
        serverSocket.close();
    }
}
