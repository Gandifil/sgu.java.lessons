import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Car {
    public int id;

    public String model;

    public String manufacturer;

    public String year;

    Car(){}

    public String getDescription()
    {
        return String.format("%s %s %s", manufacturer, model, year);
    }
}
