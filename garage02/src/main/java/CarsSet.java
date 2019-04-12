import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CarsSet {
    public final List<Car> cars;

    CarsSet(){
        cars = loadFromDB();
    }

    private List<Car> loadFromDB(){
        try{
            List<Car> cars = new LinkedList<>();
            Statement statement = DBConnection.INSTANCE.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cars");
            while (resultSet.next()) {
                Car car = new Car();
                car.id = resultSet.getInt("id");
                car.manufacturer = resultSet.getString("manufacturer").replace(" ", "");
                car.model = resultSet.getString("model").replace(" ", "");
                car.year = resultSet.getString("year").replace(" ", "");
                cars.add(car);
            }
            return cars;
        }
        catch (NullPointerException e){
            System.err.println("Нет подключения к базе данных!");
            e.printStackTrace();
        }
        catch (SQLException e){
            System.err.println("Невозможно совершить запрос к ассортименту машин!");
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    private List<Car> load(String filename){
        try{
            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(new File(filename), Car[].class));
        }
        catch (FileNotFoundException e){
            System.err.println("Файл cars.json не найден. Проверьте корневую папку проекта.");
            System.err.println(e.getMessage());
        }
        catch (IOException e){
            System.err.println("Проблемы с парсингом cars.json. Проверьте файл.");
            System.err.println(e.getMessage());
        }
        return new LinkedList<>();
    }
}
