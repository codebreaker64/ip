package devin.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representation of an event task.
 */
public class Event extends Task {
    private static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs a new instance of Event with the specified description, from, to and isDone.
     *
     * @param description task description.
     * @param from        start of task duration.
     * @param to          end of task duration.
     * @param isDone      whether the task is completed or not.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from.format(FORMATTER1) + " | " + to.format(FORMATTER1);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(FORMATTER2) + " to: " + to.format(FORMATTER2) + ")";
    }
}
