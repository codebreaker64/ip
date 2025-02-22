package devin.command;

import java.io.IOException;

import devin.Devin;
import devin.exception.DevinException;
import devin.storage.Storage;
import devin.task.Task;
import devin.task.TaskList;
import devin.ui.Ui;

/**
 * Representation of a command.
 */
public class Command {

    /**
     * A bye command to exit.
     *
     * @return an exit message.
     */
    public static String byeCommand() {
        return Ui.printExit();
    }

    /**
     * A list command to list all tasks.
     *
     * @param list of all tasks.
     * @return a message showing all the tasks in the list.
     */
    public static String listCommand(TaskList list) {
        return list.listTasks();
    }

    /**
     * A mark command to mark the task as completed in the list.
     *
     * @param list of all tasks.
     * @param texts of user input as a String array.
     * @param storage storing all tasks data.
     * @return task mark message.
     * @throws DevinException if there is any error regarding user input.
     * @throws IOException if there is any error regarding the file.
     */
    public static String markCommand(TaskList list, String[] texts, Storage storage)
            throws DevinException, IOException {
        if (list.getTasks().isEmpty()) {
            throw new DevinException("There is no task in the list!");
        } else if (texts.length != 2) {
            throw new DevinException("Please choose a task number");
        }
        int index = Integer.parseInt((texts[1])) - 1;
        if (index > list.getTasks().size() - 1 || index < 0) {
            throw new DevinException("Please choose a valid task number from 1 to "
                    + list.getTasks().size());
        }
        if (list.getTasks().get(index).getStatusIcon().equals("X")) {
            throw new DevinException("Task has already been marked!");
        }
        list.handleMark(index);
        storage.editFile(list.getTasks());
        return Ui.printMark(list.getTasks().get(index).toString());
    }

    /**
     * A unmark command to unmark the task as uncompleted in the list.
     *
     * @param list of all tasks.
     * @param texts of user input as a String array.
     * @param storage storing all tasks data.
     * @return task unmark message.
     * @throws DevinException if there is any error regarding user input.
     * @throws IOException if there is any error regarding the file.
     */
    public static String unmarkCommand(TaskList list, String[] texts, Storage storage)
            throws DevinException, IOException {
        if (list.getTasks().isEmpty()) {
            throw new DevinException("There is no task in the list!");
        } else if (texts.length != 2) {
            throw new DevinException("Please choose a task number");
        }
        int index = Integer.parseInt((texts[1])) - 1;
        if (index > list.getTasks().size() - 1 || index < 0) {
            throw new DevinException("Please choose a valid task number from 1 to "
                    + list.getTasks().size());
        }
        if (list.getTasks().get(index).getStatusIcon().equals(" ")) {
            throw new DevinException("Task has already been unmarked!");
        }
        list.handleUnmark(index);
        storage.editFile(list.getTasks());
        return Ui.printUnmark(list.getTasks().get(index).toString());
    }

    /**
     * A delete command to delete the task from the list.
     *
     * @param list of all tasks.
     * @param texts of user input as a String array.
     * @param storage storing all tasks data.
     * @return task delete message.
     * @throws DevinException if there is any error regarding user input.
     * @throws IOException if there is any error regarding the file.
     */
    public static String deleteCommand(TaskList list, String[] texts, Storage storage)
            throws DevinException, IOException {
        if (list.getTasks().isEmpty()) {
            throw new DevinException("There is no task in the list!");
        } else if (texts.length != 2) {
            throw new DevinException("Please choose a task number");
        }
        int index = Integer.parseInt((texts[1])) - 1;
        if (index > list.getTasks().size() - 1 || index < 0) {
            throw new DevinException("Please choose a valid task number from 1 to "
                    + list.getTasks().size());
        }
        Task temp = list.getTasks().get(index);
        list.deleteTask(index);
        storage.editFile(list.getTasks());
        return Ui.printDelete(temp.toString(), list.getTasks().size());
    }

    /**
     * A todo command to create a new todo task.
     *
     * @param list of all tasks.
     * @param texts of user input as a String array.
     * @param storage storing all tasks data.
     * @return task added message
     * @throws DevinException if there is any error regarding user input.
     * @throws IOException if there is any error regarding the file.
     */
    public static String todoCommand(TaskList list, String[] texts, Storage storage)
            throws DevinException, IOException {
        texts[0] = "";
        list.addTask(Devin.Type.todo, String.join(" ", texts), storage);
        return Ui.printAdd(list.getTasks().get(list.getTasks().size() - 1).toString(), list.getTasks().size());
    }

    /**
     * A deadline command to create a new deadline task.
     *
     * @param list of all tasks.
     * @param texts of user input as a String array.
     * @param storage storing all tasks data.
     * @return task added message.
     * @throws DevinException if there is any error regarding user input.
     * @throws IOException if there is any error regarding the file.
     */
    public static String deadlineCommand(TaskList list, String[] texts, Storage storage)
            throws DevinException, IOException {
        texts[0] = "";
        list.addTask(Devin.Type.deadline, String.join(" ", texts), storage);
        return Ui.printAdd(list.getTasks().get(list.getTasks().size() - 1).toString(), list.getTasks().size());
    }

    /**
     * A event command to create a new event task.
     *
     * @param list of all tasks.
     * @param texts of user input as a String array.
     * @param storage storing all tasks data.
     * @return task added message.
     * @throws DevinException if there is any error regarding user input.
     * @throws IOException if there is any error regarding the file.
     */
    public static String eventCommand(TaskList list, String[] texts, Storage storage)
            throws DevinException, IOException {
        texts[0] = "";
        list.addTask(Devin.Type.event, String.join(" ", texts), storage);
        return Ui.printAdd(list.getTasks().get(list.getTasks().size() - 1).toString(), list.getTasks().size());
    }

    /**
     * A find command to find all tasks in the list that matches the keyword.
     *
     * @param list of all tasks.
     * @param texts of user input as a String array.
     * @return a message showing all tasks found.
     * @throws DevinException if there is any error regarding user input.
     */
    public static String findCommand(TaskList list, String[] texts) throws DevinException {
        if (list.getTasks().isEmpty()) {
            throw new DevinException("There is no task in the list!");
        } else if (texts.length == 1) {
            throw new DevinException("Please type in a keyword");
        }
        texts[0] = "";
        return list.findTask(String.join(" ", texts));
    }

    /**
     * A schedule command to find all free time slot and display it to user.
     *
     * @param list of all tasks.
     * @return a message showing all free time slot.
     */
    public static String scheduleCommand(TaskList list) {
        return list.getTimedTasks();
    }
}
