package devin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import devin.exception.DevinException;
import devin.parser.Parser;
import devin.storage.Storage;

public class DevinTest {

    @TempDir
    Path tempDir;

    private Path tempFile;
    private Storage storage;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = tempDir.resolve("devin_test.txt");
        storage = new Storage(tempFile);
    }

    @Test
    public void storage_append_success() throws IOException {
        String task1 = "Task1";
        String task2 = "Task2";
        String task3 = "Task3";

        storage.appendTask(task1);
        storage.appendTask(task2);
        storage.appendTask(task3);

        String fileContent = Files.readString(tempFile);
        assertTrue(fileContent.contains(task1), "Task1 was not appended successfully");
        assertTrue(fileContent.contains(task2), "Task2 was not appended successfully");
        assertTrue(fileContent.contains(task3), "Task3 was not appended successfully");
    }

    @Test
    public void parser_parseInput_success() throws DevinException {
        String input = "meeting /from 2/2/2025 1000 /to 2/2/2025 1200";
        String expectedTaskName = "meeting";
        String expectedFromDate = "2/2/2025 1000";
        String expectedToDate = "2/2/2025 1200";

        String[] parsedTask = Parser.parseInput(Devin.Type.event, input);

        assertNotNull(parsedTask, "Parsed task should not be null");
        assertEquals(3, parsedTask.length, "Parsed array should have exactly 3 elements");
        assertEquals(expectedTaskName, parsedTask[0].trim(), "Task name was not parsed correctly");
        assertEquals(expectedFromDate, parsedTask[1].trim(), "From date was not parsed correctly");
        assertEquals(expectedToDate, parsedTask[2].trim(), "To date was not parsed correctly");

        input = "return book /by 6/6/2025 1400";
        expectedTaskName = "return book";
        String expectedByDate = "6/6/2025 1400";

        parsedTask = Parser.parseInput(Devin.Type.deadline, input);

        assertNotNull(parsedTask, "Parsed task should not be null");
        assertEquals(2, parsedTask.length, "Parsed array should have exactly 3 elements");
        assertEquals(expectedTaskName, parsedTask[0].trim(), "Task name was not parsed correctly");
        assertEquals(expectedByDate, parsedTask[1].trim(), "By date was not parsed correctly");
    }
}

