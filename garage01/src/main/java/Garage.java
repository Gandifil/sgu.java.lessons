import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Garage {
    private List<Car> cars = new LinkedList<Car>();

    public void add(Car car){
        cars.add(car);
    }

    public void remove(int index){
        cars.remove(index);
    }
    public void remove(Car car){
        cars.remove(car);
    }

    public List<String> getStatus(){
        return cars.stream().map(car -> car.getDescription()).collect(Collectors.toList());
    }
}
