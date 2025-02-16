package devin.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representation of a deadline task.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    protected LocalDateTime by;

    /**
     * Constructs a new instance of Deadline with the specified description. by and isDone.
     *
     * @param description task description.
     * @param by          task deadline.
     * @param isDone      whether the task is completed or not.
     */
    public Deadline(String description, LocalDateTime by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public LocalDateTime getEndTime() {
        return by; 
    }

    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by.format(FORMATTER1);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(FORMATTER2) + ")";
    }
}
