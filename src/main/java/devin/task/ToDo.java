package devin.task;

public class ToDo extends Task {

    /**
     * Constructs a new instance of ToDo with the specified description and isDone.
     *
     * @param description task description.
     * @param isDone      whether the task is completed or not.
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
