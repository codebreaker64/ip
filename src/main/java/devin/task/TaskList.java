package devin.task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import devin.Devin;
import devin.exception.DevinException;
import devin.parser.Parser;
import devin.storage.Storage;

/**
 * Representation of a task list.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs a new instance of TaskList with the specified store.
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
    public void addTask(Devin.Type type, String input, Storage storage) throws DevinException, IOException {
        Task task;
        String[] temp = Parser.parseInput(type, input);
        assert temp.length > 0 : "There should be at least one task.";
        switch (type) {
        case todo:
            if (input.trim().isEmpty()) {
                throw new DevinException("Oi! The description of a todo cannot be empty");
            }
            task = new ToDo(input.trim(), false);
            break;
        case deadline:
            task = new Deadline(temp[0].trim(), Parser.parseDate(temp[1].trim()), false);
            break;
        case event:
            task = new Event(temp[0].trim(), Parser.parseDate(temp[1].trim()),
                    Parser.parseDate(temp[2].trim()), false);
            break;
        default:
            throw new DevinException("Invalid task type");
            //Fallthrough
        }
        tasks.add(task);
        storage.appendTask(task.toFileString());
    }

    /**
     * Lists out all the tasks currently in the task list.
     */
    public String listTasks() {
        StringBuilder out = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            out.append(i + 1).append(". ").append(tasks.get(i).toString()).append("\n");
        }
        assert !out.isEmpty() : "There is nothing in out.";
        return out.toString();
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
     * Unmarks the specified task number as uncompleted.
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
    public String findTask(String keyword) {
        StringBuilder out = new StringBuilder("Here are the matching tasks in your Task lists:\n");
        String result = tasks.stream()
                .filter(task -> {
                    String taskName = task.name.toLowerCase();
                    String keywordLower = keyword.trim().toLowerCase();
                    String regex = "\\b" + Pattern.quote(keywordLower) + "\\b";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(taskName);
                    return matcher.find();
                })
                .map(task -> task.toString())
                .collect(Collectors.joining("\n", out.toString(), ""));
        return result;
    }

    /**
     * Get the task list.
     *
     * @return TaskList
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Find all the free time slot that the user can schedule.
     *
     * @return a message of all free time slot
     */
    public String getTimedTasks() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Task> timedTasks = tasks.stream()
                .filter(task -> task.getStartTime() != null && task.getEndTime() != null)
                .sorted(Comparator.comparing(Task::getStartTime))
                .collect(Collectors.toCollection(ArrayList::new));
        StringBuilder freeSlots = new StringBuilder();
        LocalDateTime previousEndTime = now;
        for (Task task : timedTasks) {
            if (previousEndTime.isBefore(task.getStartTime())) {
                freeSlots.append("Free slot from ")
                        .append(previousEndTime.format(formatter))
                        .append(" to ")
                        .append(task.getStartTime().format(formatter))
                        .append("\n"); // Newline separator
            }
            previousEndTime = task.getEndTime();
        }
        freeSlots.append("Free slot from ")
                .append(previousEndTime.format(formatter))
                .append(" to forever")
                .append("\n");
        return freeSlots.toString().trim();
    }
}
