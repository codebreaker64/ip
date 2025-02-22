package devin.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import devin.Devin;
import devin.exception.DevinException;

/**
 * Representation of a parser.
 */
public class Parser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d/M/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Splits the user input text into a String array via spaces.
     *
     * @param input user input text.
     * @return the split input.
     * @throws DevinException If input is empty.
     */
    public static String[] parseCommand(String input) throws DevinException {
        if (input.trim().isEmpty()) {
            throw new DevinException("Please type a valid command");
        }
        return input.split(" ");
    }

    /**
     * Converts the date and time input from String to LocalDateTime.
     *
     * @param input date and time
     * @return the converted input.
     */
    public static LocalDateTime parseDate(String input) throws DevinException {
        assert isValidDate(input) : "Invalid date.";
        LocalDateTime date = LocalDateTime.parse(input, FORMATTER);
        if (date.isBefore(LocalDateTime.now())) {
            throw new DevinException("Please input date and time that is not in the past");
        }
        return LocalDateTime.parse(input, FORMATTER);
    }

    /**
     * Splits the task detail input into task description, and deadline or duration.
     *
     * @param type  type of task.
     * @param input task detail.
     * @return the split input.
     * @throws DevinException If input is empty or input is missing keywords.
     */
    public static String[] parseInput(devin.Devin.Type type, String input) throws DevinException {
        String[] temps = null;
        if (type == Devin.Type.todo) {
            if (input.trim().isEmpty()) {
                throw new DevinException("Oi! The description of a " + type + " cannot be empty");
            }
        } else if (type == Devin.Type.deadline) {
            if (!input.contains("/by")) {
                throw new DevinException("My god! please follow this format deadline description /by d/m/yyyy HHmm");
            }
            temps = input.trim().split("/by");
            if (temps[0].trim().isEmpty()) {
                throw new DevinException("Oi! The description of a " + type + " cannot be empty");
            }
            if (temps.length == 1 || !isValidDate(temps[1].trim())) {
                throw new DevinException(
                        "Date time is invalid. Please choose a valid date and type in this format (d/M/yyyy HHmm)");
            }
        } else if (type == Devin.Type.event) {
            if (!input.contains("/from") || !input.contains("/to")) {
                throw new DevinException(
                        "My god! please follow this format event description /from d/m/yyyy HHmm /to d/m/yyyy HHmm");
            }
            temps = input.split("/from | /to");
            if (temps[0].trim().isEmpty()) {
                throw new DevinException("Oi! The description of a " + type + " cannot be empty");
            }
            if (temps.length == 1 || !isValidDate(temps[1].trim()) || !isValidDate(temps[2].trim())) {
                throw new DevinException(
                        "Date time is invalid. Please choose a valid date and type in this format (d/M/yyyy HHmm)");
            }
            if (LocalDateTime.parse(temps[1].trim(), FORMATTER).isAfter(LocalDateTime.parse(temps[2].trim(),
                    FORMATTER))) {
                throw new DevinException("/from date time should be before /to date time");
            }
        }
        return temps;
    }

    /**
     * Checks if the input can be converted to LocalDateTime in a specify format.
     *
     * @param dateString date and time input as String.
     * @return if the dateString can be converted to LocalDateTime.
     */
    public static boolean isValidDate(String dateString) {
        try {
            LocalDateTime.parse(dateString, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
