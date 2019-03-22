import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CarsSet {
    public final List<Car> cars;

    CarsSet(){
        cars = load("cars.json");
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
