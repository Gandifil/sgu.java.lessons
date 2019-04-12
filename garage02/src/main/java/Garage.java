import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Garage {
    public void add(Car car){
        try{
            List<Car> cars = new LinkedList<>();
            Statement statement = DBConnection.INSTANCE.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("INSERT INTO garage VALUES(%d)", car.id));
        }
        catch (NullPointerException e){
            System.err.println("Нет подключения к базе данных!");
            e.printStackTrace();
        }
        catch (SQLException e){
            System.err.println("Невозможно вставить в таблицу запись о новой машине!");
            e.printStackTrace();
        }
    }

    public void remove(Car car)
    {
        try{
            List<Car> cars = new LinkedList<>();
            Statement statement = DBConnection.INSTANCE.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("DELETE FROM garage WHERE car_id = %d", car.id));
        }
        catch (NullPointerException e){
            System.err.println("Нет подключения к базе данных!");
            e.printStackTrace();
        }
        catch (SQLException e){
            System.err.println("Невозможно вставить в таблицу запись о новой машине!");
            e.printStackTrace();
        }
    }

    public List<String> getStatus(){
        List<Car> cars = new LinkedList<>();
        try{
            List<Car> cars = new LinkedList<>();
            Statement statement = DBConnection.INSTANCE.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT cars.*\n" +
                    "FROM cars\n" +
                    "INNER JOIN garage\n" +
                    "ON cars.id = garage.car_id");
            while (resultSet.next()) {
                Car car = new Car();
                car.id = resultSet.getInt("id");
                car.manufacturer = resultSet.getString("manufacturer").replace(" ", "");
                car.model = resultSet.getString("model").replace(" ", "");
                car.year = resultSet.getString("year").replace(" ", "");
                cars.add(car);
            }
        }
        catch (NullPointerException e){
            System.err.println("Нет подключения к базе данных!");
            e.printStackTrace();
        }
        catch (SQLException e){
            System.err.println("Невозможно совершить запрос к ассортименту машин!");
            e.printStackTrace();
        }
        return cars.stream().map(car -> car.getDescription()).collect(Collectors.toList());
    }
}
