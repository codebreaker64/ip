package devin.task;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import devin.Devin;
import devin.exception.DevinException;
import devin.parser.Parser;
import devin.storage.Storage;

public class TaskList {
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
     * @param type    type of task.
     * @param input   task detail.
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
