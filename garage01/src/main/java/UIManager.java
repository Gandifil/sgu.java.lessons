import javax.swing.*;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

class UIButton{
    public String name;
    public Runnable exec;

    UIButton(String name, Runnable exec){
        this.name = name;
        this.exec = exec;
    }
}

public class UIManager {
    private Garage garage = new Garage();
    private CarsSet carData = new CarsSet();
    private Scanner scan = new Scanner(System.in);

    public void startMenu(){
        String intro = "Главное меню. Вы будете постоянно возвращаться сюда.";
        UIButton[] buttons = new UIButton[]{
                new UIButton("Просмотреть гараж", () -> showGarage()),
                new UIButton("Добавить в гараж машину", () -> addCar()),
                new UIButton("Удалить машину из гаража", () -> removeCar()),
        };
        uiForm(intro, buttons, false);
    }

    private void uiForm(String intro, UIButton[] buttons, boolean isReturned){
        int response = 0;
        do {
            System.out.println(intro);
            System.out.println("0. Выход");

            int i = 0;
            for (UIButton button: buttons)
                System.out.println(String.format("%d. %s", ++i, button.name));

            response = scan.nextInt();
            if (response <= buttons.length && response != 0)
            {
                buttons[response - 1].exec.run();
                if (isReturned)
                    return;
            }
        } while (response != 0);
    }

    private void showGarage(){
        System.out.println("Машины в гараже:");
        List<String> cars = garage.getStatus();
        cars.forEach(line -> System.out.println(line));
        if (cars.size() == 0)
            System.out.println("Пусто");
        System.out.println("Нажмите enter, чтобы вернуться обратно в меню");
        scan.nextLine();
        scan.nextLine();
    }

    private void addCar(){
        String intro = "Выберите машину, которую хотите добавить в гараж.";
        UIButton[] buttons = carData.cars.stream()
                .map(car -> new UIButton(car.getDescription(), ()-> garage.add(car)))
                .toArray(UIButton[]::new);
        uiForm(intro, buttons, true);
    }

    private void removeCar(){
        String intro = "Выберите машину, которую хотите убрать из гаража.";
        UIButton[] buttons = carData.cars.stream()
                .map(car -> new UIButton(car.getDescription(), ()-> garage.remove(car)))
                .toArray(UIButton[]::new);
        uiForm(intro, buttons, true);
    }
}