import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

class ClientInfo{
    public String name;
    public int roomId;
    public boolean isConnected;
}

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket socketListener = new ServerSocket(1234);

            while (true) {
                Socket client = socketListener.accept();
                new ConnectionHandler(client); //Создаем новый поток, которому передаем сокет
            }
        } catch (SocketException e) {
            System.err.println("Socket exception");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O exception");
            e.printStackTrace();
        }
    }
}
