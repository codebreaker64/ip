import java.util.ArrayList;
import java.util.Scanner;

public class Devin{
    public static ArrayList<Task> store = new ArrayList<>();
    public static int storeIndex = 0;
    public static void main(String[] args) {

        String logo = " ____             _\n" +
                "|  _ \\  _____   _(_)_ __\n" +
                "| | | |/ _ \\ \\ / / | '_ \\\n" +
                "| |_| |  __/\\ V /| | | | |\n" +
                "|____/ \\___| \\_/ |_|_| |_|\n";
        System.out.println(logo);
        greet();
        Scanner scan = new Scanner(System.in);
        while (true) {
            String text = scan.nextLine();
            try {
                if(text.trim().isEmpty()) {
                    throw new DevinException("Please type a valid command");
                }
                String[] texts = text.split(" ");
                if (text.equals("bye")) {
                    exit();
                    break;
                } else if (text.equals("list")) {
                    list();
                } else if (texts[0].equals("mark")) {

                    if (storeIndex == 0 ) {
                        throw new DevinException("There is no task in the list!");
                    } else if(texts.length != 2) {
                        throw new DevinException("Please type a choose a task number");
                    }
                    int index = Integer.parseInt((texts[1])) - 1;
                    if(index > storeIndex + 1|| index < 0) {
                        throw new DevinException("Please choose a valid task number from 1 to " + storeIndex);
                    }
                    store.get(index).mark();
                    System.out.println("____________________________________________________________");
                    System.out.println("Nice! I've marked this task as done:\n " + store.get(index).toString());
                    System.out.println("____________________________________________________________");
                } else if (texts[0].equals("unmark")) {
                    if (storeIndex == 0 ) {
                        throw new DevinException("There is no task in the list!");
                    } else if(texts.length != 2) {
                        throw new DevinException("Please type a choose a task number");
                    }
                    int index = Integer.parseInt((texts[1])) - 1;
                    if(index > storeIndex + 1|| index < 0) {
                        throw new DevinException("Please choose a valid task number from 1 to " + storeIndex);
                    }
                    store.get(index).unmark();
                    System.out.println("____________________________________________________________");
                    System.out.println("OK, I've marked this task as not done yet:\n  " + store.get(index).toString());
                    System.out.println("____________________________________________________________");
                } else if (texts[0].equals("delete")) {
                    if (storeIndex == 0 ) {
                        throw new DevinException("There is no task in the list!");
                    } else if(texts.length != 2) {
                        throw new DevinException("Please type a choose a task number");
                    }
                    int index = Integer.parseInt((texts[1])) - 1;
                    if(index > storeIndex + 1|| index < 0) {
                        throw new DevinException("Please choose a valid task number from 1 to " + storeIndex);
                    }
                    Task temp = store.get(index);
                    store.remove(index);
                    storeIndex--;
                    System.out.println("____________________________________________________________");
                    System.out.println("Noted. I've removed this task:\n  " +  temp.toString() + "\nNow you have " + storeIndex + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (texts[0].equals("todo")) {
                    texts[0] = "";
                    add('t', String.join(" ", texts));
                } else if (texts[0].equals("deadline")) {
                    texts[0] = "";
                    add('d', String.join(" ", texts));
                } else if (texts[0].equals("event")) {
                    texts[0] = "";
                    add('e', String.join(" ", texts));
                }
            } catch (DevinException e) {
                System.out.println(e.getMessage());
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
        System.out.println("____________________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    public static void echo(String input) {
        System.out.println("____________________________________________________________");
        System.out.println(input);
        System.out.println("____________________________________________________________");
    }

    public static void add(char type, String input) throws DevinException {
        switch(type) {
            case 't':
                if(input.trim().isEmpty()) {
                    throw new DevinException("Oi! The description of a todo cannot be empty");
                }
                store.add(storeIndex, new ToDo(input.trim()));
                storeIndex++;
                break;
            case 'd':
                if(input.trim().isEmpty()) {
                    throw new DevinException("Oi! The description of a deadline cannot be empty");
                }
                String[] temp = input.split("/by");
                if(temp.length == 1) {
                    throw new DevinException("My god! please put the /by before the date/time");
                }
                store.add(storeIndex, new Deadline(temp[0].trim(), temp[1].trim()));
                storeIndex++;
                break;
            case 'e':
                if(input.trim().isEmpty()) {
                    throw new DevinException("Oi! The description of a event cannot be empty");
                }
                String[] temp1 = input.split("/from");
                if(temp1.length == 1) {
                    throw new DevinException("My god! please put the /from before the date/time");
                }
                String[] temp2 = temp1[1].split("/to");
                if(temp2.length == 1) {
                    throw new DevinException("My god! please put the /to before the date/time");
                }
                store.add( storeIndex, new Event(temp1[0].trim(), temp2[0].trim(), temp2[1].trim()));
                storeIndex++;
                break;
            default:
                store.add(storeIndex, new Task(input.trim()));
                storeIndex++;
                break;
        }
        System.out.println("____________________________________________________________");
        System.out.println("Got it. I've added this task:\n  " + store.get(storeIndex-1).toString() + "\nNow you have " + storeIndex + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public static void list() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for(int i = 0; i < storeIndex; i++){
            System.out.println(i+1 + ". " + store.get(i).toString());
        }
        System.out.println("____________________________________________________________");
    }

}

//Inspired by https://www.geeksforgeeks.org/user-defined-custom-exception-in-java/
class DevinException extends Exception {
    public DevinException(String message) {
        super(message);
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
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.name;
    }
}

class Deadline extends Task {

    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

class ToDo extends Task {

    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Event extends Task {

    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}