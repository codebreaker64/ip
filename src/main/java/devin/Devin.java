package devin;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.*;
import java.io.*;
import java.time.*;
import java.time.format.*;

class Devin {

    public static Path filePath = Paths.get("src/main/java/data/devin.txt");


    private static Storage storage;
    private static Tasklist tasks;

    enum Type {
        todo,
        deadline,
        event
    }

    public static void main(String[] args) {
        storage = new Storage(filePath);
        tasks = new Tasklist(storage.retrieve());
        Scanner scan = new Scanner(System.in);
        Ui.greet();
        while (true) {
            String text = scan.nextLine();
            try {
                String[] texts = Parser.parseCommand(text);
                int index;
                switch (texts[0]) {
                    case "bye":
                        Ui.exit();
                        return;
                    case "list":
                        tasks.list();
                        break;
                    case "mark":
                        if (tasks.store.isEmpty()) {
                            throw new DevinException("There is no task in the list!");
                        } else if (texts.length != 2) {
                            throw new DevinException("Please choose a task number");
                        }
                        index = Integer.parseInt((texts[1])) - 1;
                        if (index > tasks.store.size() + 1 || index < 0) {
                            throw new DevinException("Please choose a valid task number from 1 to " + tasks.store.size());
                        }
                        tasks.handleMark(index);
                        storage.edit(tasks.store);
                        Ui.showMark(tasks.store.get(index).toString());
                        break;
                    case "unmark":
                        if (tasks.store.isEmpty()) {
                            throw new DevinException("There is no task in the list!");
                        } else if (texts.length != 2) {
                            throw new DevinException("Please type a choose a task number");
                        }
                        index = Integer.parseInt((texts[1])) - 1;
                        if (index > tasks.store.size() + 1 || index < 0) {
                            throw new DevinException("Please choose a valid task number from 1 to " + tasks.store.size());
                        }
                        tasks.handleUnmark(index);
                        storage.edit(tasks.store);
                        Ui.showUnmark(tasks.store.get(index).toString());
                        break;
                    case "delete":
                        if (tasks.store.isEmpty()) {
                            throw new DevinException("There is no task in the list!");
                        } else if (texts.length != 2) {
                            throw new DevinException("Please type a choose a task number");
                        }
                        index = Integer.parseInt((texts[1])) - 1;
                        if (index > tasks.store.size() + 1 || index < 0) {
                            throw new DevinException("Please choose a valid task number from 1 to " + tasks.store.size());
                        }
                        Task temp = tasks.store.get(index);
                        tasks.delete(index);
                        storage.edit(tasks.store);
                        Ui.showDelete(temp.toString(), tasks.store.size());
                        break;
                    case "todo":
                        texts[0] = "";
                        tasks.add(Devin.Type.todo, String.join(" ", texts), storage);
                        Ui.showAdd(tasks.store.get(tasks.store.size() - 1).toString(), tasks.store.size());
                        break;
                    case "deadline":
                        texts[0] = "";
                        tasks.add(Devin.Type.deadline, String.join(" ", texts), storage);
                        Ui.showAdd(tasks.store.get(tasks.store.size() - 1).toString(), tasks.store.size());
                        break;
                    case "event":
                        texts[0] = "";
                        tasks.add(Devin.Type.event, String.join(" ", texts), storage);
                        Ui.showAdd(tasks.store.get(tasks.store.size() - 1).toString(), tasks.store.size());
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            } catch (DevinException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

//Inspired by https://www.geeksforgeeks.org/user-defined-custom-exception-in-java/
class DevinException extends Exception {
    public DevinException(String message) {
        super(message);
    }
}

class Ui {

    public static void greet() {
        String logo = " ____             _\n" +
                "|  _ \\  _____   _(_)_ __\n" +
                "| | | |/ _ \\ \\ / / | '_ \\\n" +
                "| |_| |  __/\\ V /| | | | |\n" +
                "|____/ \\___| \\_/ |_|_| |_|\n";
        System.out.println(logo);
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

    public static void showMark(String taskName) {
        System.out.println("____________________________________________________________");
        System.out.println("Nice! I've marked this task as done:\n " + taskName);
        System.out.println("____________________________________________________________");
    }

    public static void showUnmark(String taskName) {
        System.out.println("____________________________________________________________");
        System.out.println("OK, I've marked this task as not done yet:\n  " + taskName);
        System.out.println("____________________________________________________________");
    }

    public static void showDelete(String temp, int size) {
        System.out.println("____________________________________________________________");
        System.out.println("Noted. I've removed this task:\n  " + temp + "\nNow you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public static void showAdd(String taskName, int size) {
        System.out.println("____________________________________________________________");
        System.out.println("Got it. I've added this task:\n  " + taskName + "\nNow you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

}

class Storage{
    public Path parentDir;
    public Path filePath;

    public Storage (Path filePath) {
        this.filePath = filePath;
        this.parentDir = filePath.getParent();
    }

    public ArrayList<Task> retrieve() {
        ArrayList<Task> store = new ArrayList<>();
        try {
            if (!Files.exists(parentDir)) {
                throw new DevinException("Data folder does not exist.");
            } else if (!Files.exists(filePath)) {
                throw new DevinException("devin.txt does not exist.");
            }
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] input = line.split(" \\| ");
                if (input[0].equals("T")) {
                    store.add(new ToDo(input[2], input[1].equals("X")));
                } else if (input[0].equals("D")) {
                    store.add(new Deadline(input[2], Parser.parseDate(input[3]), input[1].equals("X")));
                } else if (input[0].equals("E")) {
                    store.add(new Event(input[2], Parser.parseDate(input[3]), Parser.parseDate(input[4]), input[1].equals("X")));
                }
            }
        } catch (DevinException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        return store;
    }

    public void edit(ArrayList<Task> store) {
        try (FileWriter writer = new FileWriter(filePath.toString())) {
            for (Task task : store) {
                writer.write(task.toFileString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void append(String taskName) {
        try (FileWriter writer = new FileWriter(filePath.toString(), true)) {
            writer.write(taskName + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}

class Parser{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    public static String[] parseCommand(String input) throws DevinException {
        if (input.trim().isEmpty()) {
            throw new DevinException("Please type a valid command");
        }
        return input.split(" ");
    }

    public static LocalDateTime parseDate(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        return LocalDateTime.parse(input, formatter);
    }
    public static String[] parseInput(devin.Devin.Type type, String input) throws DevinException {
        if (input.trim().isEmpty()) {
            throw new DevinException("Oi! The description of a" + type + " cannot be empty");
        }
        String[] temp = null;
        if(type == Devin.Type.deadline) {
            if (!input.contains("/by")) {
                throw new DevinException("My god! please follow this format deadline task /by d/m/yyyy HHmm");
            }
            temp = input.split("/by");
            if (!isValidDate(temp[1].trim())) {
                throw new DevinException("Date time format is incorrect. Please type in this format (d/M/yyyy HHmm)");
            }
        } else if (type == Devin.Type.event) {
            if (!input.contains("/from") || !input.contains("/to")) {
                throw new DevinException("My god! please follow this format event task /from d/m/yyyy HHmm /to d/m/yyyy HHmm");
            }
            temp = input.split("/from | /to");
            if(!isValidDate(temp[1].trim()) && !isValidDate(temp[2].trim())) {
                throw new DevinException("Date time format is incorrect. Please type in this format (d/M/yyyy HHmm)");
            }
        }
        return temp;
    }

    public static boolean isValidDate(String dateString) {
        try {
            LocalDateTime.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;


        }
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

class ToDo extends Task {

    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Deadline extends Task {
    public static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    public static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by.format(formatter1);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(formatter2) + ")";
    }
}

class Event extends Task {
    public static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    public static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from.format(formatter1) + " | " + to.format(formatter1);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(formatter2) + " to: " + to.format(formatter2) + ")";
    }
}

class Tasklist {

    public ArrayList<Task> store;

    public Tasklist(ArrayList<Task> store) {
        this.store = store;
    }

    public void add(Devin.Type type, String input, Storage storage) throws DevinException {
        Task task;
        String[] temp = null;
        try {
            switch (type) {
                case todo:
                    if (input.trim().isEmpty()) {
                        throw new DevinException("Oi! The description of a todo cannot be empty");
                    }
                    task = new ToDo(input.trim(), false);
                    store.add(task);
                    storage.append(task.toFileString());
                    break;
                case deadline:
                    temp = Parser.parseInput(type, input);
                    task = new Deadline(temp[0].trim(), Parser.parseDate(temp[1].trim()), false);
                    store.add(task);
                    storage.append(task.toFileString());
                    break;
                case event:
                    temp = Parser.parseInput(type, input);
                    task = new Event(temp[0].trim(), Parser.parseDate(temp[1].trim()), Parser.parseDate(temp[2].trim()), false);
                    store.add(task);
                    storage.append(task.toFileString());
                    break;
            }
        } catch (DevinException e) {
            throw new DevinException(e.getMessage());
        }
    }

    public void list() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < store.size(); i++) {
            System.out.println(i + 1 + ". " + store.get(i).toString());
        }
        System.out.println("____________________________________________________________");
    }

    public void handleMark(int index) {
        store.get(index).mark();
    }

    public void handleUnmark(int index) {
        store.get(index).unmark();
    }

    public void delete(int index) {
        store.remove(index);
    }
}

