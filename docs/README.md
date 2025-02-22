# Devin User Guide

## Table of Contents
1. [Introduction](#introduction)
2. [Quick Start](#quick-start)
3. [Features](#features)
4. [Important Note](#important-note)
5. [Command Summary](#command-summary)
6. [FAQ](#faq)
7. [Acknowledgements](#acknowledgements)

## Introduction
Welcome to **Devin**, your personal chatbot for managing tasks efficiently. Devin helps you track **to-dos, deadlines, events**, and more. This guide provides instructions on how to use Devin effectively.

## Quick Start

1. Ensure you have **Java 17 or above** installed on your computer.
   **Mac users**: Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
3. Download the latest `.jar` file from [here](https://github.com/codebreaker64/ip/releases/download/A-Release/devin.jar).
4. Move the downloaded `.jar` file to your preferred home folder for Devin.
5. Run Devin using one of the following methods:

    - **Double-click** the JAR file.

   Or, use the terminal:

    - Open the terminal/command prompt.
    - Navigate to the folder where the JAR file is stored:

      ```bash
      cd /path/to/folder
      ```

    - Run the following command:

      ```bash
      java -jar Devin.jar
      ```

6. Once Devin starts, explore its features by entering commands. 🚀

## Features
Devin supports a variety of commands to help you manage your schedule:

### Add a To-Do
Adds a simple task without a specific deadline.

**Format:** `todo <Description>`

**Example:**
```
todo Buy groceries
```

### Add an Event
Adds an event with a start and end time.

**Format:** `event <Description> /from d/m/yyyy HHmm /to d/m/yyyy HHmm`

**Example:**
```
event Team meeting /from 15/2/2025 1400 /to 15/2/2025 1600
```

### Add a Deadline
Adds a task with a specific due date and time.

**Format:** `deadline <Description> /by d/m/yyyy HHmm`

**Example:**
```
deadline Project submission /by 20/2/2025 2359
```

### List Tasks
Displays all tasks currently stored.

**Format:** `list`

**Example:**
```
list
```

### Mark Task as Completed
Marks a task as completed.

**Format:** `mark <Index>`

**Example:**
```
mark 2
```

### Unmark Task as Incomplete
Marks a task as not completed.

**Format:** `unmark <Index>`

**Example:**
```
unmark 2
```

### Delete a Task
Removes a task from the list.

**Format:** `delete <Index>`

**Example:**
```
delete 3
```

### Find a Task
Searches for tasks that contain the given keyword.

**Format:** `find <Keyword>`

**Example:**
```
find meeting
```

### Find Free Time Slot
Finds the next available time slot in your schedule.

**Format:** `schedule`

**Example:**
```
schedule
```

### Exit Devin
Ends the program.

**Format:** `bye`

**Example:**
```
bye
```

## Important Note
- All dates and times must be entered in the format `d/M/yyyy HHmm` (e.g., `15/2/2025 1400`).
- Invalid date entries (e.g., `30/2/2025`) will result in an error.
- Tasks are automatically saved in a text file within a dedicated folder located in the same directory as your JAR file.
  Do not modify the text file, move the folder, or relocate the JAR file, as this may cause data loss or corruption.

## Command Summary

| Command                   | Format                                                      | Description                                          |
|---------------------------|-------------------------------------------------------------|------------------------------------------------------|
| Add a To-Do               | `todo <Description>`                                        | Adds a simple task without a specific deadline.      |
| Add an Event              | `event <Description> /from d/m/yyyy HHmm /to d/m/yyyy HHmm` | Adds an event with a start and end time.             |
| Add a Deadline            | `deadline <Description> /by d/m/yyyy HHmm`                  | Adds a task with a specific due date and time.       |
| List Tasks                | `list`                                                      | Displays all tasks currently stored.                 |
| Mark Task as Completed    | `mark <Index>`                                              | Marks a task as completed.                           |
| Unmark Task as Incomplete | `unmark <Index>`                                            | Marks a task as not completed.                       |
| Delete a Task             | `delete <Index>`                                            | Removes a task from the list.                        |
| Find a Task               | `find <Keyword>`                                            | Searches for tasks that contain the given keyword.   |
| Find Free Time Slot       | `schedule`                                                  | Finds the next available time slot in your schedule. |
| Exit Devin                | `bye`                                                       | Ends the program.                                    |

## FAQ
### Q: What happens if I enter an invalid command?
A: Devin will notify you that the command is unrecognized and prompt you to enter a valid command.

### Q: Can I edit a task after adding it?
A: No, but you can delete the incorrect task and add a new one.

### Q: Does Devin save my tasks after I exit?
A: Yes, tasks are saved automatically and will be available the next time you start Devin.

### Q: Can I use multiple keywords in the `find` command?
A: No, the `find` command only supports searching by a single keyword.

## Acknowledgements
- Inspired by Duke chatbot from [NUS SE-EDU](https://github.com/nus-cs2103-AY2425S2/ip)

