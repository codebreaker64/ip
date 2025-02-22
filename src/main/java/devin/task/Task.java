package devin.task;

import java.time.LocalDateTime;

/**
 * Representation of a Task.
 */
public class Task {
    protected String name;
    protected boolean isDone;

    /**
     * Constructs a new instance of Task with the specified name and isDone.
     *
     * @param name   task description.
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
    public LocalDateTime getStartTime() {
        return null;
    }
    public LocalDateTime getEndTime() {
        return null;
    }

    public String toFileString() {
        return getStatusIcon() + " | " + this.name;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.name;
    }
}
