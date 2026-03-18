
public class App {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("   Simple Java Maven Demo App   ");
        System.out.println("=================================");

        int a = 10;
        int b = 20;

        int sum = add(a, b);

        System.out.println("Sum of " + a + " and " + b + " is: " + sum);

        System.out.println("Application Executed Successfully!");
    }

    public static int add(int x, int y) {
        return x + y;
    }
}