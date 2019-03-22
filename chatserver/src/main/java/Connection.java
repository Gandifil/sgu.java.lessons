import java.net.Socket;

public class Connection extends Thread {
    private Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
        this.start();
    }

    public void run() {

    }
}
