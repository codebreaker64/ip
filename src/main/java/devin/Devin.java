package devin;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Devin {

    public static Path filePath = Paths.get("src/main/java/data/devin.txt");
    private static Storage storage;
    private static TaskList list;

    enum Type {
        todo,
        deadline,
        event
    }

    public static void main(String[] args) {
        storage = new Storage(filePath);
        list = new TaskList(storage.retrieveTasks());
        Scanner scan = new Scanner(System.in);
        
        Ui.printGreet();
        
        while (true) {
            String text = scan.nextLine();
            try {
                String[] texts = Parser.parseCommand(text);
                int index;
                switch (texts[0]) {
                case "bye":
                    Ui.printExit();
                    return;
                case "list":
                    list.listTasks();
                    break;
                case "mark":
                    if (list.tasks.isEmpty()) {
                        throw new DevinException("There is no task in the list!");
                    } else if (texts.length != 2) {
                        throw new DevinException("Please choose a task number");
                    }
                    index = Integer.parseInt((texts[1])) - 1;
                    if (index > list.tasks.size() + 1 || index < 0) {
                        throw new DevinException("Please choose a valid task number from 1 to " 
                                + list.tasks.size());
                    }
                    list.handleMark(index);
                    storage.editFile(list.tasks);
                    Ui.printMark(list.tasks.get(index).toString());
                    break;
                case "unmark":
                    if (list.tasks.isEmpty()) {
                        throw new DevinException("There is no task in the list!");
                    } else if (texts.length != 2) {
                        throw new DevinException("Please type a choose a task number");
                    }
                    index = Integer.parseInt((texts[1])) - 1;
                    if (index > list.tasks.size() + 1 || index < 0) {
                        throw new DevinException("Please choose a valid task number from 1 to " 
                                + list.tasks.size());
                    }
                    list.handleUnmark(index);
                    storage.editFile(list.tasks);
                    Ui.printUnmark(list.tasks.get(index).toString());
                    break;
                case "delete":
                    if (list.tasks.isEmpty()) {
                        throw new DevinException("There is no task in the list!");
                    } else if (texts.length != 2) {
                        throw new DevinException("Please type a choose a task number");
                    }
                    index = Integer.parseInt((texts[1])) - 1;
                    if (index > list.tasks.size() + 1 || index < 0) {
                        throw new DevinException("Please choose a valid task number from 1 to " 
                                + list.tasks.size());
                    }
                    Task temp = list.tasks.get(index);
                    list.deleteTask(index);
                    storage.editFile(list.tasks);
                    Ui.printDelete(temp.toString(), list.tasks.size());
                    break;
                case "todo":
                    texts[0] = "";
                    list.addTask(Devin.Type.todo, String.join(" ", texts), storage);
                    Ui.printAdd(list.tasks.get(list.tasks.size() - 1).toString(), list.tasks.size());
                    break;
                case "deadline":
                    texts[0] = "";
                    list.addTask(Devin.Type.deadline, String.join(" ", texts), storage);
                    Ui.printAdd(list.tasks.get(list.tasks.size() - 1).toString(), list.tasks.size());
                    break;
                case "event":
                    texts[0] = "";
                    list.addTask(Devin.Type.event, String.join(" ", texts), storage);
                    Ui.printAdd(list.tasks.get(list.tasks.size() - 1).toString(), list.tasks.size());
                    break;
                case "find":
                    if (list.tasks.isEmpty()) {
                        throw new DevinException("There is no task in the list!");
                    } else if (texts.length == 1) {
                        throw new DevinException("Please type in a keyword");
                    }
                    texts[0] = "";
                    list.findTask(String.join(" ", texts));
                    break;
                default:
                    throw new DevinException("Unknown command");
                    //Fallthrough
                }
            } catch (DevinException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

//Solution inspired by https://www.geeksforgeeks.org/user-defined-custom-exception-in-java/
class DevinException extends Exception {

    /**
     * Constructs a new instance of DevinException with the specified message.
     *
     * @param message error message.
     */
    public DevinException(String message) {
        super(message);
    }
}

class Ui {

    /**
     * Prints the greeting message.
     */
    public static void printGreet() {
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

    /**
     * Prints the exit message.
     */
    public static void printExit() {
        System.out.println("____________________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints out the input.
     *
     * @param input user input text.
     */
    public static void printEcho(String input) {
        System.out.println("____________________________________________________________");
        System.out.println(input);
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints out the mark message with the specified task description.
     *
     * @param taskName task description.
     */
    public static void printMark(String taskName) {
        System.out.println("____________________________________________________________");
        System.out.println("Nice! I've marked this task as done:\n " + taskName);
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints out the unmark message with the specified task description.
     *
     * @param taskName task description.
     */
    public static void printUnmark(String taskName) {
        System.out.println("____________________________________________________________");
        System.out.println("OK, I've marked this task as not done yet:\n  " + taskName);
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints out the delete message with the specified task detail and task list size.
     *
     * @param temp task detail.
     * @param size task list size.
     */
    public static void printDelete(String temp, int size) {
        System.out.println("____________________________________________________________");
        System.out.println("Noted. I've removed this task:\n  " + temp 
                + "\nNow you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints out the add message with the specified task description and task list size.
     *
     * @param taskName task description.
     * @param size task list size.
     */
    public static void printAdd(String taskName, int size) {
        System.out.println("____________________________________________________________");
        System.out.println("Got it. I've added this task:\n  " + taskName 
                + "\nNow you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

}

class Storage{
    public Path parentDir;
    public Path filePath;

    /**
     * Constructs a new instance of Storage with the specified file path.
     *
     * @param filePath relative path to storage file.
     */
    public Storage (Path filePath) {
        this.filePath = filePath;
        this.parentDir = filePath.getParent();
    }

    /**
     * Returns the task list retrieved from storage file.
     * If the file is empty, it will return an empty list.
     *
     * @return task list.
     */
    public ArrayList<Task> retrieveTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        
        try {
            if (!Files.exists(parentDir)) {
                throw new DevinException("Data folder does not exist.");
            } else if (!Files.exists(filePath)) {
                throw new DevinException("devin.txt does not exist.");
            }
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] inputs = line.split(" \\| ");
                if (inputs[0].equals("T")) {
                    tasks.add(new ToDo(inputs[2], inputs[1].equals("X")));
                } else if (inputs[0].equals("D")) {
                    tasks.add(new Deadline(inputs[2], Parser.parseDate(inputs[3]), inputs[1].equals("X")));
                } else if (inputs[0].equals("E")) {
                    tasks.add(new Event(inputs[2], Parser.parseDate(inputs[3]), Parser.parseDate(inputs[4]),
                            inputs[1].equals("X")));
                }
            }
        } catch (DevinException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Rewrites the storage file with the edited information.
     *
     * @param tasks task list.
     */
    public void editFile(ArrayList<Task> tasks) {
        try (FileWriter writer = new FileWriter(filePath.toString())) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Appends the new task detail into the storage file.
     *
     * @param taskName task detail.
     */
    public void appendTask(String taskName) {
        try (FileWriter writer = new FileWriter(filePath.toString(), true)) {
            writer.write(taskName + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}

class Parser{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    /**
     * Splits the user input text into a String array via spaces.
     *
     * @param input user input text.
     * @return the split input.
     * @throws DevinException If input is empty.
     */
    public static String[] parseCommand(String input) throws DevinException {
        if (input.trim().isEmpty()) {
            throw new DevinException("Please type a valid command");
        }
        return input.split(" ");
    }

    /**
     * Converts the date and time input from String to LocalDateTime.
     *
     * @param input date and time
     * @return the converted input.
     */
    public static LocalDateTime parseDate(String input) {
        return LocalDateTime.parse(input, FORMATTER);
    }

    /**
     * Splits the task detail input into task description, and deadline or duration.
     *
     * @param type type of task.
     * @param input task detail.
     * @return the split input.
     * @throws DevinException If input is empty or input is missing keywords.
     */
    public static String[] parseInput(devin.Devin.Type type, String input) throws DevinException {
        if (input.trim().isEmpty()) {
            throw new DevinException("Oi! The description of a" + type + " cannot be empty");
        }
        String[] temps = null;
        if (type == Devin.Type.deadline) {
            if (!input.contains("/by")) {
                throw new DevinException("My god! please follow this format deadline task /by d/m/yyyy HHmm");
            }
            temps = input.split("/by");
            if (!isValidDate(temps[1].trim())) {
                throw new DevinException("Date time format is incorrect. Please type in this format (d/M/yyyy HHmm)");
            }
        } else if (type == Devin.Type.event) {
            if (!input.contains("/from") || !input.contains("/to")) {
                throw new DevinException(
                        "My god! please follow this format event task /from d/m/yyyy HHmm /to d/m/yyyy HHmm");
            }
            temps = input.split("/from | /to");
            if (!isValidDate(temps[1].trim()) && !isValidDate(temps[2].trim())) {
                throw new DevinException("Date time format is incorrect. Please type in this format (d/M/yyyy HHmm)");
            }
        }
        return temps;
    }

    /**
     * Checks if the input can be converted to LocalDateTime in a specify format.
     *
     * @param dateString date and time input as String.
     * @return if the dateString can be converted to LocalDateTime.
     */
    public static boolean isValidDate(String dateString) {
        try {
            LocalDateTime.parse(dateString, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

class Task {
    protected String name;
    protected boolean isDone;

    /**
     * Constructs a new instance of Task with the specified name and isDone.
     *
     * @param name task description.
     * @param isDone whether the task is completed or not.
     */
    public Task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    /**
     * Returns X if task is done or nothing otherwise.
     *
     * @return the status icon.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the task description.
     *
     * @return task description.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Marks the task as completed,
     */
    public void markTask() {
        this.isDone = true;
    }

    /**
     * Unmarks the task as uncompleted.
     */
    public void unmarkTask() {
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

    /**
     * Constructs a new instance of ToDo with the specified description and isDone.
     *
     * @param description task description.
     * @param isDone whether the task is completed or not.
     */
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

    /**
     * Constructs a new instance of Deadline with the specified description. by and isDone.
     *
     * @param description task description.
     * @param by task deadline.
     * @param isDone whether the task is completed or not.
     */
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

    /**
     * Constructs a new instance of Event with the specified description, from, to and isDone.
     *
     * @param description task description.
     * @param from start of task duration.
     * @param to end of task duration.
     * @param isDone whether the task is completed or not.
     */
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

class TaskList {
    public ArrayList<Task> tasks;

    /**
     * Constructs a new instance of Tasklist with the specified store.
     *
     * @param tasks task list retrieved from storage file.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Add the new task into the task list.
     *
     * @param type type of task.
     * @param input task detail.
     * @param storage instance of Storage object.
     * @throws DevinException if the todo description is empty.
     */
    public void addTask(Devin.Type type, String input, Storage storage) throws DevinException {
        Task task;
        String[] temp = null;
        
        try {
            switch (type) {
            case todo:
                if (input.trim().isEmpty()) {
                    throw new DevinException("Oi! The description of a todo cannot be empty");
                }
                task = new ToDo(input.trim(), false);
                tasks.add(task);
                storage.appendTask(task.toFileString());
                break;
            case deadline:
                temp = Parser.parseInput(type, input);
                task = new Deadline(temp[0].trim(), Parser.parseDate(temp[1].trim()), false);
                tasks.add(task);
                storage.appendTask(task.toFileString());
                break;
            case event:
                temp = Parser.parseInput(type, input);
                task = new Event(temp[0].trim(), Parser.parseDate(temp[1].trim()),
                        Parser.parseDate(temp[2].trim()), false);
                tasks.add(task);
                storage.appendTask(task.toFileString());
                break;
            default:
                throw new DevinException("Invalid task type");
                //Fallthrough
            }
        } catch (DevinException e) {
            throw new DevinException(e.getMessage());
        }
    }

    /**
     * Lists out all the tasks currently in the task list.
     */
    public void listTasks() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i + 1 + ". " + tasks.get(i).toString());
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Marks the specified task number as completed.
     *
     * @param index task number.
     */
    public void handleMark(int index) {
        tasks.get(index).markTask();
    }

    /**
     * Unmarks the specified task number as incompleted.
     *
     * @param index task number.
     */
    public void handleUnmark(int index) {
        tasks.get(index).unmarkTask();
    }

    /**
     * Deletes the specified task number from the list.
     *
     * @param index task number
     */
    public void deleteTask(int index) {
        tasks.remove(index);
    }

    /**
     * Prints out all the task that contains the keyword.
     *
     * @param keyword keyword to filter the task list.
     */
    public void findTask(String keyword) {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the matching tasks in your listTasks:");
        int i = 1;
        for (Task task : tasks) {
            String taskName = task.name.toLowerCase();
            String keywordLower = keyword.trim().toLowerCase();


            String regex = "\\b" + Pattern.quote(keywordLower) + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(taskName);

            if (matcher.find()) {
                System.out.println(i + "." + task.toString());
                i++;
            }
        }
        System.out.println("____________________________________________________________");
    }
}

