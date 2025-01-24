import java.util.Scanner;

public class Devin {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("____________________________________________________________");
        greet();
        Scanner scan = new Scanner(System.in);
        while (true) {
            String text = scan.nextLine();
            if(text.equals("bye")) {
                exit();
                break;
            }
            echo(text);
        }
    }

    public static void greet() {
        System.out.println("Hello! I'm, Devin");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
    }

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    public static void echo(String input) {
        System.out.println(input);
    }

}


