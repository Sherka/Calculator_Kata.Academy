import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class Main {
    Integer first, second, result;
    String operator;
    Boolean isRoman = false;
    private final static TreeMap<Integer, String> map = new TreeMap<>();
    static {
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.doMath();
    }

    //Стартовый метод для расчета переданного выражения
    private void doMath () {
        try {
            String[] splitStr = readString().split(" ");
            checkRomanNumbers(splitStr);
            if (isRoman) {
                first = fromRoman(splitStr[0]);
                second = fromRoman(splitStr[2]);
            } else {
                first = Integer.parseInt(splitStr[0]);
                second = Integer.parseInt(splitStr[2]);
            }
            operator = splitStr[1];
            if(first > 10 || second > 10) {
                System.out.println("You can use numbers from 1 to 10");
                return;
            }
            mathOperations();
            if (isRoman) {
               if (result <= 0) {
                   System.out.println("Roman result can't be less or equals 0");
                   return;
               }
            }
            printResult();
        } catch (NumberFormatException ex) {
            System.out.println("You can use only integers");
        } catch (NotOnlyRomanNums ex) {
            System.out.println("You can use only Arabic or Roman numbers");
        } catch (NotAllowedMathOperation ex) {
            System.out.println("Use only allowed math operators: +, -, +, /");
        } catch (IOException ex) {
            if (ex.getMessage().isBlank())
                System.out.println("Something went wrong while reading line from console");
            else System.out.println(ex.getMessage());
        }
    }

    //Чтение строки из консоли
    private String readString () throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter math expression:");
        String expString = br.readLine();
        if (expString == null || expString.isBlank() || expString.split(" ").length != 3)
            throw new IOException("Check you math expression. Example: \"3 + 4\" or \"I + I\"");
        if (checkContainsOperator(expString))
            throw new IOException(("Your expression must contain two numbers and one math operator between them"));
        return expString;
    }

    //Выполение операции по заданному оператору
    private void mathOperations () throws NotAllowedMathOperation {
        switch (operator) {
            case "+" -> result = first + second;
            case "-" -> result = first - second;
            case "*" -> result = first * second;
            case "/" -> result = first / second;
            default -> throw new NotAllowedMathOperation();
        }
    }

    //Вывод результата после вычислений
    private void printResult () {
        String str;
        str = isRoman ? toRoman(result) : result.toString();
        System.out.println(str);
    }

    //Конвертация арабских чисел в римские
    private String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }

    //Конвертация введенного римской цифры в арабскую
    private Integer fromRoman(String romanString) {
        return switch (romanString) {
            case "I" -> 1;
            case "II" -> 2;
            case "III" -> 3;
            case "IV" -> 4;
            case "V" -> 5;
            case "VI" -> 6;
            case "VII" -> 7;
            case "VIII" -> 8;
            case "IX" -> 9;
            case "X" -> 10;
            default -> throw new NumberFormatException("You can use Roman numbers from I to X");
        };
    }

    //Проверка на наличие и правильность математического оператора
    private boolean checkContainsOperator (String readLine) {
        String[] splitStr = readLine.split(" ");
        return !"+-*/".contains(splitStr[1]);
    }

    //Проверка на совпадение двух аргументов одной системе чисел
    private void checkRomanNumbers (String[] numbers) throws NotOnlyRomanNums {
        boolean checkFirst, checkSecond;
        checkFirst = numbers[0].contains("I") || numbers[0].contains("V") || numbers[0].contains("X");
        checkSecond = numbers[2].contains("I") || numbers[2].contains("V") || numbers[2].contains("X");
        if (checkFirst != checkSecond) throw new NotOnlyRomanNums();
        isRoman = checkFirst;
    }
}