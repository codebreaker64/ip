package devin;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import devin.command.Command;
import devin.exception.DevinException;
import devin.parser.Parser;
import devin.storage.Storage;
import devin.task.TaskList;

/**
 * Representation of Devin.
 */
public class Devin {

    private static final Path FILEPATH = Paths.get("src/main/java/data/devin.txt");
    private static Storage storage;
    private static TaskList list;

    /**
     * Type of task.
     */
    public enum Type {
        todo,
        deadline,
        event
    }

    public static void main(String[] args) throws DevinException, IOException {
        storage = new Storage(FILEPATH);
        list = new TaskList(storage.retrieveTasks());
    }

    /**
     * Get response for the GUI based on the command user has given.
     *
     * @param text inputted by user.
     * @return a message based on command user has given.
     * @throws DevinException if there is any error regarding user input.
     * @throws IOException if there is any error regarding.
     */
    public String getResponse(String text) throws DevinException, IOException {
        String[] texts = Parser.parseCommand(text);
        return switch (texts[0]) {
        case "bye" -> Command.byeCommand();
        case "list" -> Command.listCommand(list);
        case "mark" -> Command.markCommand(list, texts, storage);
        case "unmark" -> Command.unmarkCommand(list, texts, storage);
        case "delete" -> Command.deleteCommand(list, texts, storage);
        case "todo" -> Command.todoCommand(list, texts, storage);
        case "deadline" -> Command.deadlineCommand(list, texts, storage);
        case "event" -> Command.eventCommand(list, texts, storage);
        case "find" -> Command.findCommand(list, texts);
        case "schedule" -> Command.scheduleCommand(list);
        default -> throw new DevinException("""
                I'm sorry, but I didn't catch that. Here are the commands I understand: \s
                - `bye` \s
                - `list` \s
                - `mark` \s
                - `unmark` \s
                - `delete` \s
                - `todo` \s
                - `deadline` \s
                - `event` \s
                - `find` \s
                - `schedule` \s
                
                Try one of these!""");
        };
    }

}

