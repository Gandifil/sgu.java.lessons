import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBConnection {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "gandifil";
    static final String PASS = "";

    public static final DBConnection INSTANCE = new DBConnection();

    private Connection connection;

    private DBConnection(){
        try{
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            generate();
            System.out.println("БД подключена успешно!");
        }
        catch (Exception e){
            System.err.println("Ошибка подключения к БД!");
            e.printStackTrace();
        }
    }

    public void generate(){
        try{
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("DROP ALL OBJECTS DELETE FILES");
            stmt.executeUpdate("CREATE TABLE cars (id INTEGER not NULL AUTO_INCREMENT, model VARCHAR(255), " +
                    "manufacturer VARCHAR(255), year VARCHAR(255), PRIMARY KEY ( id ))");
            stmt.executeUpdate("CREATE TABLE garagecars (id INTEGER not NULL AUTO_INCREMENT," +
                    " car_id INT REFERENCES cars(id), PRIMARY KEY ( id ))");

            stmt.executeUpdate("INSERT INTO cars(manufacturer, model, year) VALUES ('Toyota', 'Corolla', '1997')");
            stmt.executeUpdate("INSERT INTO cars(manufacturer, model, year) VALUES ('Hyundai', 'Solaris', '2018')");
            stmt.executeUpdate("INSERT INTO cars(manufacturer, model, year) VALUES ('Kia', 'Rio', '2017')");
        }
        catch(SQLException e){
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

    public List<Car> getCars(){
        List<Car> cars = new LinkedList<>();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cars");
            while (resultSet.next()) {
                Car car = new Car();
                car.id = resultSet.getInt("id");
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


    public List<Car> getGarageCars(){
        List<Car> cars = new LinkedList<>();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM garagecars, cars WHERE garagecars.car_id = cars.id");
            while (resultSet.next()) {
                Car car = new Car();
                car.id = resultSet.getInt("garagecars.id");
                car.manufacturer = resultSet.getString("cars.manufacturer");
                car.model = resultSet.getString("cars.model");
                car.year = resultSet.getString("cars.year");
                cars.add(car);
            }
        }
        catch (SQLException e){
            System.err.println("Statement 'getGarageCars' Failed");
            e.printStackTrace();
        }

        return cars;
    }

    public void addCarToGarage(Car car){
        try{
            Statement statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO garagecars(car_id) VALUES(%d)", car.id));
        }
        catch (SQLException e){
            System.err.println("Statement 'addCarToGarage' Failed");
            e.printStackTrace();
        }
    }

    public void deleteCarFromGarage(Car car){
        try{
            Statement statement = connection.createStatement();
            statement.execute(String.format("DELETE FROM garagecars WHERE id = %d", car.id));
        }
        catch (SQLException e){
            System.err.println("Statement 'addCarToGarage' Failed");
            e.printStackTrace();
        }
    }
}
