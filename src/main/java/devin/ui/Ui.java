package devin.ui;

/**
 * Representation of a ui.
 */
public class Ui {

    /**
     * Prints the greeting message.
     */
    public static String printGreet() {
        return "Hello! I'm, Devin\nWhat can I do for you?";
    }

    /**
     * Prints the exit message.
     */
    public static String printExit() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Prints out the mark message with the specified task description.
     *
     * @param taskName task description.
     */
    public static String printMark(String taskName) {
        return "Nice! I've marked this task as done:\n " + taskName;
    }

    /**
     * Prints out the unmark message with the specified task description.
     *
     * @param taskName task description.
     */
    public static String printUnmark(String taskName) {
        return "OK, I've marked this task as not done yet:\n  " + taskName;
    }

    /**
     * Prints out the delete message with the specified task detail and task list size.
     *
     * @param temp task detail.
     * @param size task list size.
     */
    public static String printDelete(String temp, int size) {
        return "Noted. I've removed this task:\n  " + temp + "\nNow you have " + size + " tasks in the list.";
    }

    /**
     * Prints out the add message with the specified task description and task list size.
     *
     * @param taskName task description.
     * @param size     task list size.
     */
    public static String printAdd(String taskName, int size) {
        return "Got it. I've added this task:\n  " + taskName + "\nNow you have " + size + " tasks in the list.";
    }

}
