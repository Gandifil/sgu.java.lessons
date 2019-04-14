import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    Scanner scan = new Scanner(System.in);
	    System.out.println("Здравствуйте! Это приложение калькулятор!");

	    while(true) {
	        try {
				System.out.println("Введите выражение для вычисления: ");
	            String str = scan.nextLine();
	            if (str == "")
	                break;
	            Expression expr = Expression.generate(str);
				System.out.println(str + " = " + expr.result());
            }
	        catch (IllegalStateException e){
                System.err.println(e.getMessage());
            }
        }

        System.out.println("Работа успешно завершена!");
    }
}
