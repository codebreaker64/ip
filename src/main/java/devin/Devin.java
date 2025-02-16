package devin;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import devin.exception.DevinException;
import devin.parser.Parser;
import devin.storage.Storage;
import devin.task.Task;
import devin.task.TaskList;
import devin.ui.Ui;

/**
 * Representation of Devin.
 */
public class Devin {

    private static final Path FILEPATH = Paths.get("src/main/java/data/devin.txt");
    private static Storage storage;
    private static TaskList list;

    /**
     * Type of task.
     */
    public enum Type {
        todo,
        deadline,
        event
    }

    public static void main(String[] args) throws DevinException, IOException {
        storage = new Storage(FILEPATH);
        list = new TaskList(storage.retrieveTasks());
    }

    public String getResponse(String text) throws DevinException, IOException {
        String[] texts = Parser.parseCommand(text);
        assert texts.length > 0: "There should be at least one command.";
        int index;
        switch (texts[0]) {
        case "bye":
            return Ui.printExit();
        case "list":
            return list.listTasks();
        case "mark":
            if (list.getTasks().isEmpty()) {
                throw new DevinException("There is no task in the list!");
            } else if (texts.length != 2) {
                throw new DevinException("Please choose a task number");
            }
            index = Integer.parseInt((texts[1])) - 1;
            if (index > list.getTasks().size() + 1 || index < 0) {
                throw new DevinException("Please choose a valid task number from 1 to "
                        + list.getTasks().size());
            }
            list.handleMark(index);
            storage.editFile(list.getTasks());
            return Ui.printMark(list.getTasks().get(index).toString());
        case "unmark":
            if (list.getTasks().isEmpty()) {
                throw new DevinException("There is no task in the list!");
            } else if (texts.length != 2) {
                throw new DevinException("Please type a choose a task number");
            }
            index = Integer.parseInt((texts[1])) - 1;
            if (index > list.getTasks().size() + 1 || index < 0) {
                throw new DevinException("Please choose a valid task number from 1 to "
                        + list.getTasks().size());
            }
            list.handleUnmark(index);
            storage.editFile(list.getTasks());
            return Ui.printUnmark(list.getTasks().get(index).toString());
        case "delete":
            if (list.getTasks().isEmpty()) {
                throw new DevinException("There is no task in the list!");
            } else if (texts.length != 2) {
                throw new DevinException("Please type a choose a task number");
            }
            index = Integer.parseInt((texts[1])) - 1;
            if (index > list.getTasks().size() + 1 || index < 0) {
                throw new DevinException("Please choose a valid task number from 1 to "
                        + list.getTasks().size());
            }
            Task temp = list.getTasks().get(index);
            assert temp != null: "The list does not contain this task.";
            list.deleteTask(index);
            storage.editFile(list.getTasks());
            return Ui.printDelete(temp.toString(), list.getTasks().size());
        case "todo":
            texts[0] = "";
            list.addTask(Devin.Type.todo, String.join(" ", texts), storage);
            return Ui.printAdd(list.getTasks().get(list.getTasks().size() - 1).toString(), list.getTasks().size());
        case "deadline":
            texts[0] = "";
            list.addTask(Devin.Type.deadline, String.join(" ", texts), storage);
            return Ui.printAdd(list.getTasks().get(list.getTasks().size() - 1).toString(), list.getTasks().size());
        case "event":
            texts[0] = "";
            list.addTask(Devin.Type.event, String.join(" ", texts), storage);
            return Ui.printAdd(list.getTasks().get(list.getTasks().size() - 1).toString(), list.getTasks().size());
        case "find":
            if (list.getTasks().isEmpty()) {
                throw new DevinException("There is no task in the list!");
            } else if (texts.length == 1) {
                throw new DevinException("Please type in a keyword");
            }
            texts[0] = "";
            return list.findTask(String.join(" ", texts));
        default:
            throw new DevinException("Unknown command");
        }
    }

}

