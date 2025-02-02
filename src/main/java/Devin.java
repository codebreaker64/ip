import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.*;
import java.io.*;

public class Devin{
    public static ArrayList<Task> store = new ArrayList<>();
    public static int storeIndex = 0;

    public static Path filePath = Paths.get("src/main/java/data/devin.txt");
    public static Path parentDir = filePath.getParent();

    enum Type {
        todo,
        deadline,
        event
    }

    public static void main(String[] args) {

        try {
            if (!Files.exists(parentDir)) {
                throw new DevinException("Data folder does not exist.");
            } else if (!Files.exists(filePath)) {
                throw new DevinException("devin.txt does not exist.");
            }
            BufferedReader reader = new BufferedReader( new FileReader(filePath.toString()));
            String line;
            while((line = reader.readLine()) != null) {
                String[] input = line.split(" \\| ");
                if(input[0].equals("T")) {
                    store.add(storeIndex, new ToDo(input[2], input[1].equals("X")));
                    storeIndex++;
                } else if(input[0].equals("D")) {
                    store.add(storeIndex, new Deadline(input[2], input[3],input[1].equals("X")));
                    storeIndex++;
                } else if(input[0].equals("E")) {
                    store.add(storeIndex, new Event(input[2], input[3], input[4], input[1].equals("X")));
                    storeIndex++;
                }
            }
        } catch(DevinException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

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
                        throw new DevinException("Please choose a task number");
                    }
                    int index = Integer.parseInt((texts[1])) - 1;
                    if(index > storeIndex + 1|| index < 0) {
                        throw new DevinException("Please choose a valid task number from 1 to " + storeIndex);
                    }
                    store.get(index).mark();
                    FileWriter writer = new FileWriter(filePath.toString());
                    for(Task task : store) {
                        writer.write(task.toFileString() + "\n");
                    }
                    writer.close();
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
                    FileWriter writer = new FileWriter(filePath.toString());
                    for(Task task : store) {
                        writer.write(task.toFileString() + "\n");
                    }
                    writer.close();
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

                    FileWriter writer = new FileWriter(filePath.toString());
                    for(Task task : store) {
                        writer.write(task.toFileString() + "\n");
                    }
                    writer.close();
                    System.out.println("____________________________________________________________");
                    System.out.println("Noted. I've removed this task:\n  " +  temp.toString() + "\nNow you have " + storeIndex + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (texts[0].equals("todo")) {
                    texts[0] = "";
                    add(Type.todo, String.join(" ", texts));
                } else if (texts[0].equals("deadline")) {
                    texts[0] = "";
                    add(Type.deadline, String.join(" ", texts));
                } else if (texts[0].equals("event")) {
                    texts[0] = "";
                    add(Type.event, String.join(" ", texts));
                }
            } catch (DevinException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
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

    public static void add(Type type, String input) throws DevinException {
        Task task;
        try (FileWriter writer = new FileWriter(filePath.toString(), true)) { // Overwrites the file
            switch(type) {
                case todo:
                    if(input.trim().isEmpty()) {
                        throw new DevinException("Oi! The description of a todo cannot be empty");
                    }
                    task = new ToDo(input.trim(), false);
                    store.add(storeIndex, task);
                    storeIndex++;
                    writer.write(task.toFileString() + "\n");
                    break;
                case deadline:
                    if(input.trim().isEmpty()) {
                        throw new DevinException("Oi! The description of a deadline cannot be empty");
                    }
                    String[] temp = input.split("/by");
                    if(temp.length == 1) {
                        throw new DevinException("My god! please put the /by before the date/time");
                    }
                    task = new Deadline(temp[0].trim(), temp[1].trim(), false);
                    store.add(storeIndex, task);
                    storeIndex++;
                    writer.write(task.toFileString() + "\n");
                    break;
                case event:
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
                    task = new Event(temp1[0].trim(), temp2[0].trim(), temp2[1].trim(), false);
                    store.add( storeIndex, task);
                    storeIndex++;
                    writer.write(task.toFileString() + "\n");
                    break;
                default:
                    store.add(storeIndex, new Task(input.trim(), false));
                    storeIndex++;
                    break;
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
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

    public Task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
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

    public String toFileString() {
        return getStatusIcon() + " | " + this.name;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.name;
    }
}

class Deadline extends Task {

    protected String by;

    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String toFileString() {
        return "D | "+ super.toFileString() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

class ToDo extends Task {

    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toFileString() {
        return "T | "+ super.toFileString();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Event extends Task {

    protected String from;
    protected String to;

    public Event(String description, String from, String to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileString() {
        return "E | "+ super.toFileString() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}