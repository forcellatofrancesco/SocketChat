import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String address = ;
        int port = ;
        String name;
        System.out.println("Write your name: ");
        name = scanner.nextLine();
        Client client = new Client(name);
        client.start(address, port);
        String message = "";
        while (!message.equals(":q")) {
            message = scanner.nextLine();
            client.send(message);
        }
        client.close();
        scanner.close();
    }
}
