import io.javalin.Javalin;
import io.javalin.websocket.WsSession;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    static final int PORT = 1234;

    private static Map<WsSession, Connection> connections = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Javalin.create()
                .enableStaticFiles("/public")
                .ws("/chat", ws -> {
                    ws.onConnect(session -> connections.put(session, new Connection(session, connections)));
                    ws.onClose((session, status, message) -> connections.get(session).close());
                    ws.onMessage((session, message) -> connections.get(session).receive(message));
                })
                .start(PORT);
    }
}
