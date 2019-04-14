import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Абстрактный класс, который представляет из себя исчисляемое выражение</p>
 * <p>Включает себя методы, являющиеся общими для всех видов выражений и проверяющие их на валидность</p>
 */
public abstract class Expression {
    public abstract float result();

    public static Expression generate(String line) {
        return create(check(removeSpaces(line)));
    }

    protected static Expression create(String line)
    {
        line = clearBrackets(line);
        if (line.isEmpty())
            throw new IllegalStateException("В ходе парсинга обнаружено пустое выражение");
        try{
            float x = Float.parseFloat(line
                    .replace("(", "")
                    .replace(")", ""));
            return new ValueExpression(x);
        }
        catch (NumberFormatException e){
            return new OperationExpression(line);
        }
    }

    private static String clearBrackets(String line){
        String regex = "^\\((.*)\\)$";
        String stepLine = line.replaceAll(regex, "$1");
        while (!line.equals(stepLine))
        {
            line = stepLine;
            stepLine = line.replaceAll(regex, "$1");
        }
        return stepLine;
    }

    private static String check(String line)
    {
        if (!checkBrackets(line))
            throw new IllegalStateException("Количество закрывающих и открывающих скобок должно быть равно!");

        int k = checkSymbols(line);

        if (k != -1)
            throw new IllegalStateException("Неразрешенный символ: \n" +
                    line + "\n" + getPointer(line.length(), k));

        k = checkOperations(line);

        if (k != -1)
            throw new IllegalStateException("Операции идут подряд (чувак, ну нельзя же так): \n" +
                    line + "\n" + getPointer(line.length(), k));

        return line;
    }

    private static String getPointer(int n, int k)
    {
        char[] line = new char[n];
        Arrays.fill(line, '_');
        line[k] = '^';
        return new String(line);
    }

    private static String removeSpaces(String line)
    {
        return line.replace(" ", "");
    }

    /**
     * <p>Проверяем строку на наличие неразрешенных символов</p>
     */
    private static int checkSymbols(String line)
    {
        Pattern pattern = Pattern.compile("[^0-9\\^\\/\\*\\-\\+\\(\\)]");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            return matcher.start();
        else return -1;
    }

    /**
     * <p>Проверяет правильность расстановки круглых скобок. Как и их кол-во, так и положение.</p>
     */
    private static boolean checkBrackets(String line)
    {
        int balance = 0;
        for (char c: line.toCharArray())
        {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0)
                throw new IllegalStateException(
                        "Закрывающая скобка закрывает непонятно что! Невозможно разделить выражение на подвыражения!\n");
        }
        return balance == 0;
    }

    /**
     * <p>Проверяем наличие нескольких символов операций подряд</p>
     */
    private static int checkOperations(String line){
        Pattern pattern = Pattern.compile("[\\^\\/\\*\\-\\+]{2,}");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            return matcher.start();
        else return -1;
    }

}

class ValueExpression extends Expression{
    public ValueExpression(float val){
        value = val;
    }

    public float result()
    {
        return value;
    }

    float value;
}


class OperationExpression extends Expression{
    Expression a, b;
    char operation;

    /**
     * <p>Пробегаемся по всей строке и ищем подходящее место
     * для разделения выражения на подвыражения и операцию</p>
     */
    public OperationExpression(String line) {
        for (int lvl = 0; lvl < 3; lvl++) {
            int brackets = 0;
            for (int i = 0; i < line.length(); i++){
                char ch = line.charAt(i);
                switch (ch){
                    case '(':
                        ++brackets;
                        break;
                    case ')':
                        --brackets;
                        break;

                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '^':
                        if (brackets == 0){
                            if (("+-".indexOf(ch) != -1 && lvl == 0) ||
                                ("*/".indexOf(ch) != -1 && lvl == 1) ||
                                ("^".indexOf(ch) != -1 && lvl == 2))
                            {
                                    operation = ch;
                                    a = Expression.create(line.substring(0, i));
                                    b = Expression.create(line.substring(i + 1));
                                    return;
                                }
                        }
                        break;
                }
            }
        }

        throw  new IllegalStateException("Невозможно распарсить строку: " + line);
    }

    /**
     * <p>Возвращает результат вычисления операции над составляющими подвыражениями.</p>
     * <p>Т.к. используется значение operation из полей класса,
     * в случае неверного значения операции вызывает IllegalStateException</p>
     * @return результат вычисления выражения
     */
    public float result() {
        switch (operation){
            case '+': return a.result() + b.result();
            case '-': return a.result() - b.result();
            case '*': return a.result() * b.result();
            case '/': return a.result() / b.result();
            case '^': return (float)Math.pow(a.result(), b.result());
            default: throw new IllegalStateException(
                    "Попытка вычислить неизвестную операцию: " + operation);
        }
    }
}