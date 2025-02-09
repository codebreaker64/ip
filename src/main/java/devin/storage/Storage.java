package devin.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import devin.task.Deadline;
import devin.task.Event;
import devin.task.Task;
import devin.task.ToDo;
import devin.exception.DevinException;
import devin.parser.Parser;

public class Storage {
    public Path parentDir;
    public Path filePath;

    /**
     * Constructs a new instance of Storage with the specified file path.
     *
     * @param filePath relative path to storage file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
        this.parentDir = filePath.getParent();
    }

    /**
     * Returns the task list retrieved from storage file.
     * If the file is empty, it will return an empty list.
     *
     * @return task list.
     */
    public ArrayList<Task> retrieveTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            if (!Files.exists(parentDir)) {
                throw new DevinException("Data folder does not exist.");
            } else if (!Files.exists(filePath)) {
                throw new DevinException("devin.txt does not exist.");
            }
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] inputs = line.split(" \\| ");
                if (inputs[0].equals("T")) {
                    tasks.add(new ToDo(inputs[2], inputs[1].equals("X")));
                } else if (inputs[0].equals("D")) {
                    tasks.add(new Deadline(inputs[2], Parser.parseDate(inputs[3]), inputs[1].equals("X")));
                } else if (inputs[0].equals("E")) {
                    tasks.add(new Event(inputs[2], Parser.parseDate(inputs[3]), Parser.parseDate(inputs[4]),
                            inputs[1].equals("X")));
                }
            }
        } catch (DevinException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Rewrites the storage file with the edited information.
     *
     * @param tasks task list.
     */
    public void editFile(ArrayList<Task> tasks) {
        try (FileWriter writer = new FileWriter(filePath.toString())) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Appends the new task detail into the storage file.
     *
     * @param taskName task detail.
     */
    public void appendTask(String taskName) {
        try (FileWriter writer = new FileWriter(filePath.toString(), true)) {
            writer.write(taskName + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
