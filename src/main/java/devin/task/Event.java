package devin.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    public static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    public static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        return "E | " + super.toFileString() + " | " + from.format(formatter1) + " | " + to.format(formatter1);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(formatter2) + " to: " + to.format(formatter2) + ")";
    }
}
