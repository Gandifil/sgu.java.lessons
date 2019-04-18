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
    private Scanner scan = new Scanner(System.in);
    DBConnection conn = DBConnection.INSTANCE;

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
        List<Car> cars = conn.getGarageCars();
        cars.forEach(car -> System.out.println(car.getDescription()));
        if (cars.size() == 0)
            System.out.println("Пусто");
        else
            System.out.println("Количество машин: " + cars.size());
        System.out.println("Нажмите enter, чтобы вернуться обратно в меню");
        scan.nextLine();
        scan.nextLine();
    }

    private void addCar(){
        String intro = "Выберите машину, которую хотите добавить в гараж.";
        UIButton[] buttons = conn.getCars().stream()
                .map(car -> new UIButton(car.getDescription(), ()-> conn.addCarToGarage(car)))
                .toArray(UIButton[]::new);
        uiForm(intro, buttons, true);
    }

    private void removeCar(){
        String intro = "Выберите машину, которую хотите убрать из гаража.";
        UIButton[] buttons = conn.getGarageCars().stream()
                .map(car -> new UIButton(car.getDescription(), ()-> conn.deleteCarFromGarage(car)))
                .toArray(UIButton[]::new);
        uiForm(intro, buttons, true);
    }
}