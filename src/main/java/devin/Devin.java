package devin;

import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;

import devin.exception.DevinException;
import devin.parser.Parser;
import devin.storage.Storage;
import devin.task.Task;
import devin.task.TaskList;
import devin.ui.Ui;

public class Devin {

    public static Path filePath = Paths.get("src/main/java/data/devin.txt");
    private static Storage storage;
    private static TaskList list;

    public enum Type {
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

