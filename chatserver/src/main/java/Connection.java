import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.WsSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Connection{
    private static ObjectMapper mapper = new ObjectMapper();

    private ClientInfo _info = new ClientInfo();
    private WsSession _socket;
    private int stage = 0;
    private ClientUpdate pred_update = null;
    Map<WsSession, Connection> _clients;


    public Connection(WsSession socket, Map<WsSession, Connection> clients) {
        _clients = clients;
        _socket = socket;
        System.out.println("New connection!");
        sendAllRoom();
    }

    public void receive(String msg){
        System.out.println(msg);

        if (stage == 1){
            try{
                ClientUpdate update = mapper.readValue(msg, ClientUpdate.class);
                update.name = _info.name;
                pred_update = update;
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
                    pred_update = _info.init;
                    broadcast(mapper.writeValueAsString(_info.init));
                    sendAllActors();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String msg){
        _clients.values().stream()
                .filter(x -> x._info.roomId == _info.roomId && x._socket.isOpen() && x.pred_update != null && x != this)
                .forEach(x -> x._socket.send(msg));
        System.out.println("Broadcast! " + msg);
    }

    public void send(String msg) {
        if (_socket.isOpen()){
            _socket.send(msg);
            System.out.println("Send! " + msg);
        }
        else
            System.err.println("ERROR! SENDING ON CLOSED SOCKET");
    }

    private void sendAllRoom(){
        try{
            List<Integer> rooms = _clients.values().stream()
                    .filter(x -> x._socket.isOpen()&& x.pred_update != null && x != this)
                    .map(x -> x._info.roomId)
                    .distinct()
                    .collect(Collectors.toList());
            send(mapper.writeValueAsString(rooms));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sendAllActors(){
        _clients.values().stream()
                .filter(x -> x._info.roomId == _info.roomId && x._socket.isOpen()&& x.pred_update != null && x != this)
                .forEach(x -> {
                    try{
                        send(mapper.writeValueAsString(x.pred_update));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                });
    }

    public void close() {
        pred_update.message = "\nclose\n";
        try{
            broadcast(mapper.writeValueAsString(pred_update));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        _clients.remove(this);
        System.out.println("Close connection!");
    }
}