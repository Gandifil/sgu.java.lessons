import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBConnection {
    public static final DBConnection INSTANCE = new DBConnection();

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/java_lessons";
    private static final String USER = "postgres";
    private static final String PASS = "makasin";

    private Connection connection;

    private DBConnection(){
        try{
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Java JDBC PostgreSQL подключен успешно!");
        }
        catch (SQLException e){
            System.err.println("Ошибка подключения к БД!");
            e.printStackTrace();
        }
    }

    /**
     * <p> Если соединения с базой данных нет, вызываем NullPointerException </p>
     */
    public Connection getConnection() {
        if (connection == null)
            throw new NullPointerException();
        return connection;
    }

    public List<Car> getCars(String table){
        System.out.println("Reading car records...");
        List<Car> cars = new LinkedList<>();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public." + table);
            while (resultSet.next()) {
                Car car = new Car();
                car.manufacturer = resultSet.getString("manufacturer");
                car.model = resultSet.getString("model");
                car.year = resultSet.getString("year");
                cars.add(car);
            }
        }
        catch (SQLException e){
            System.err.println("Statement 'getCars' Failed");
            e.printStackTrace();
        }

        return cars;
    }

    public void addCarToGarage(Car car){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("INSERT INTO public.cars VALUES('%s', '%s', '%s')",
                            car.manufacturer, car.model, car.year));
        }
        catch (SQLException e){
            System.err.println("Statement 'addCarToGarage' Failed");
            e.printStackTrace();
        }
    }

    public void deleteCarFromGarage(Car car){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("DELETE FROM public.cars WHERE " +
                                    "manufacturer = '%s' AND model = '%s' AND year = '%s')",
                            car.manufacturer, car.model, car.year));
        }
        catch (SQLException e){
            System.err.println("Statement 'addCarToGarage' Failed");
            e.printStackTrace();
        }
    }
}
