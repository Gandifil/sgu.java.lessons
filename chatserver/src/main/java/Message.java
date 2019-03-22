import java.io.Serializable;

public class Message implements Serializable {
    private String author;
    private String message;

    public Message(String author, String message){
        this.author = author;
        this.message = message;
    }

    public void setOnlineUsers(String[] users) {
        this.users = users;
    }

    public String getLogin() {
        return this.login;
    }

    public String getMessage() {
        return this.message;
    }

    public String[] getUsers() {
        return this.users;
    }

    public String getDate(){
        Time tm = new Time(this.time.getTime());
        return tm.toString();
    }
}