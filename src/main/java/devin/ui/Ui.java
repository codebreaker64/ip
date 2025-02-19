package devin.ui;

/**
 * Representation of a ui.
 */
public class Ui {

    /**
     * Prints the greeting message.
     */
    public static String printGreet() {
        return "Hello! I'm Devin, your AI assistant. How can I assist you today?";
    }

    /**
     * Prints the exit message.
     */
    public static String printExit() {
        return "Goodbye! Feel free to return whenever you need assistance.";
    }

    /**
     * Prints out the mark message with the specified task description.
     *
     * @param taskName task description.
     */
    public static String printMark(String taskName) {
        return "Task marked as completed:\n  " + taskName + "\nGreat work!";
    }

    /**
     * Prints out the unmark message with the specified task description.
     *
     * @param taskName task description.
     */
    public static String printUnmark(String taskName) {
        return "Task marked as not completed:\n  " + taskName + "\nLet me know if you need further adjustments.";
    }

    /**
     * Prints out the delete message with the specified task detail and task list size.
     *
     * @param temp task detail.
     * @param size task list size.
     */
    public static String printDelete(String temp, int size) {
        return "Task removed successfully:\n  " + temp + "\nYou now have " + size + " tasks remaining.";
    }

    /**
     * Prints out the add message with the specified task description and task list size.
     *
     * @param taskName task description.
     * @param size     task list size.
     */
    public static String printAdd(String taskName, int size) {
        return "Task added successfully:\n  " + taskName + "\nYou now have " + size + " tasks in your list.";
    }

}