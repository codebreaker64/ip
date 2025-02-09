package devin.ui;

public class Ui {

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
     * @param size     task list size.
     */
    public static void printAdd(String taskName, int size) {
        System.out.println("____________________________________________________________");
        System.out.println("Got it. I've added this task:\n  " + taskName
                + "\nNow you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

}
