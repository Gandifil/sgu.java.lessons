package lessons.gandifil;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GarageController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/cars")
    public List<Car> cars() {
        DBConnection.INSTANCE.start();
        List<Car> cars = DBConnection.INSTANCE.getCars();
        DBConnection.INSTANCE.close();
        return cars;
    }

    @RequestMapping("/garages")
    public List<Garage> garages() {
        DBConnection.INSTANCE.start();
        List<Garage> garages = DBConnection.INSTANCE.getGarageCars();
        garages = garages.stream().map(garage -> (Garage)Hibernate.unproxy(garage)).collect(Collectors.toList());
        DBConnection.INSTANCE.close();
        return garages;
    }

    @RequestMapping("/add")
    public boolean add(@RequestParam int id) {
        DBConnection.INSTANCE.start();
        Car car = DBConnection.INSTANCE.s.load(Car.class, id);
        DBConnection.INSTANCE.addCarToGarage(car);
        DBConnection.INSTANCE.close();
        return true;
    }


    @RequestMapping("/delete")
    public boolean delete(@RequestParam int id) {
        DBConnection.INSTANCE.start();
        Garage garage = DBConnection.INSTANCE.s.load(Garage.class, id);
        DBConnection.INSTANCE.deleteCarFromGarage(garage);
        DBConnection.INSTANCE.close();
        return true;
    }
}