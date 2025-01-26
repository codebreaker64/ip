import java.util.Scanner;

public class Devin {
    public static Task[] store = new Task[100];
    public static int storeIndex = 0;
    public static void main(String[] args) {

        String logo = " ____             _\n" +
                "|  _ \\  _____   _(_)_ __\n" +
                "| | | |/ _ \\ \\ / / | '_ \\\n" +
                "| |_| |  __/\\ V /| | | | |\n" +
                "|____/ \\___| \\_/ |_|_| |_|\n";
        System.out.println(logo);
        System.out.println("____________________________________________________________");
        greet();
        Scanner scan = new Scanner(System.in);
        while (true) {
            String text = scan.nextLine();
            if(text.equals("bye")) {
                exit();
                break;
            } else if (text.equals("list")) {
                list();
            } else if (text.split(" ")[0].equals("mark")) {
                store[Integer.parseInt((text.split(" ")[1])) - 1].mark();
            } else if (text.split(" ")[0].equals("unmark")) {
                store[Integer.parseInt((text.split(" ")[1])) - 1].unmark();
            }else {
                add(text);
            }
        }
    }

    public static void greet() {
        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm, Devin");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
    }

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    public static void echo(String input) {
        System.out.println("____________________________________________________________");
        System.out.println(input);
        System.out.println("____________________________________________________________");
    }

    public static void add(String input) {
        store[storeIndex] = new Task(input);
        storeIndex++;
        System.out.println("____________________________________________________________");
        System.out.println("added: " + input);
        System.out.println("____________________________________________________________");
    }

    public static void list() {
        System.out.println("____________________________________________________________");
        for(int i = 0; i < storeIndex; i++){
            System.out.println(i+1 + ". [" +store[i].getStatusIcon() + "] " + store[i].getName());
        }
        System.out.println("____________________________________________________________");
    }

}

class Task {
    protected String name;
    protected boolean isDone;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public String getName() {
        return this.name;
    }

    public void mark() {
        this.isDone = true;
        System.out.println("____________________________________________________________");
        System.out.println("Nice! I've marked this task as done:\n  [" + getStatusIcon() + "] " + this.name);
        System.out.println("____________________________________________________________");
    }

    public void unmark() {
        this.isDone = false;
        System.out.println("____________________________________________________________");
        System.out.println("OK, I've marked this task as not done yet:\n  [" + getStatusIcon() + "] " + this.name);
        System.out.println("____________________________________________________________");
    }
}



