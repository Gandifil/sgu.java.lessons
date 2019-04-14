import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.WsSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class Connection{
    private static ObjectMapper mapper = new ObjectMapper();

    private ClientInfo _info = new ClientInfo();
    private WsSession _socket;
    private int stage = 0;
    Map<WsSession, Connection> _clients;

    public Connection(WsSession socket, Map<WsSession, Connection> clients) {
        _clients = clients;
        _socket = socket;
        System.out.println("New connection!");
    }

    public void receive(String msg){
        System.out.println(msg);

        if (stage == 1){
            try{
                ClientUpdate update = mapper.readValue(msg, ClientUpdate.class);
                update.name = _info.name;
                broadcast(mapper.writeValueAsString(update));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if (stage == 0){
            try{
                _info = mapper.readValue(msg, ClientInfo.class);
                if (!_info.name.isEmpty()){
                    stage++;
                    _info.init.name = _info.name;
                    broadcast(mapper.writeValueAsString(_info.init));
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String msg){
        _clients.values().stream()
                .filter(x -> x._info.roomId == _info.roomId && x._socket.isOpen() && x != this)
                .forEach(x -> x._socket.send(msg));
        System.out.println("Broadcast! " + msg);
    }

    public void send(String msg) {
        if (_socket.isOpen())
            _socket.send(msg);
        System.out.println("Send! " + msg);
    }

    public void close() {
        _clients.remove(this);
        System.out.println("Close connection!");
    }
}