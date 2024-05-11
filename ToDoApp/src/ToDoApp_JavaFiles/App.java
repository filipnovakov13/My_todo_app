package ToDoApp_JavaFiles;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {

    static String filePath = "src/ToDoList/TaskList.txt";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] allCommands = {"-l", "-a", "-c", "-r"};         // Available commands
        printUsage();                                            // Display usage instructions

        while (true) {                                           // Continue prompting the user for commands until they decide to exit

            System.out.println("\nEnter a command: ");           // Prompt the user to enter a command
            String input = scanner.nextLine().trim();

            if (input.equals("exit")) {                          // Check if the user wants to exit
                break;
            }

            String command = "";
            String argument = "";

            int firstQuoteIndex = input.indexOf('"');
            int lastQuoteIndex = input.lastIndexOf('"');

            if (firstQuoteIndex != -1 && lastQuoteIndex != -1 && firstQuoteIndex != lastQuoteIndex) {
                command = input.substring(0, firstQuoteIndex).trim();
                argument = input.substring(firstQuoteIndex + 1, lastQuoteIndex).trim();    // Extract the command and argument(s) from the input
            } else {
                int firstSpaceIndex = input.indexOf(" ");                                  // Handle the case where no arguments are provided (or no quotes are used)
                if (firstSpaceIndex != -1) {
                    command = input.substring(0, firstSpaceIndex).trim();
                    argument = input.substring(firstSpaceIndex + 1).trim();
                } else {
                    command = input;       // No spaces found, the entire input is treated as a command
                }
            }

            if (!Arrays.asList(allCommands).contains(command)) {
                System.err.println("Unsupported argument");
                printUsage();
                continue;
            }

            String[] commandArgs = {command, argument};
            try {
                handleCommand(commandArgs);
            } catch (IOException e) {
                System.err.println("Error reading/writing file");
            }
        }
        scanner.close();
    }

    private static void handleCommand(String[] args) throws IOException {
        if (args.length == 0) {
            printUsage();
            return;
        }
        if ((args[0].equals("-r") || args[0].equals("-c")) && args[1].isEmpty()) {    // Checking for each command if the necessary argument is provided
            if (args[0].equals("-r")) {
                System.out.println("Unable to remove: no index provided");
            } else if (args[0].equals("-c")) {
                System.out.println("Unable to mark as completed: no index provided");
            }
            return;
        }

        switch (args[0]) {
            case "-l":
                listTasks();
                break;
            case "-a":
                if (args[1].isEmpty()) {
                    System.err.println("Unable to add: no task provided");
                } else {
                    addTask(args[1]);
                }
                break;
            case "-c":
                completeTask(args);
                break;
            case "-r":
                removeTask(args);
                break;
            default:
                printUsage();
                break;
        }
    }

    private static void listTasks() throws IOException {
        List<String> tasks = readTasksFromFile();
        if (tasks.isEmpty()) {
            System.out.println("No todos for today! :)");
        } else {
            int lineNumber = 1;
            for (String task : tasks) {
                System.out.println(lineNumber + " - " + task);
                lineNumber++;
            }
        }
    }

    private static void addTask(String task) throws IOException {
        writeTaskToFile(task, true);
    }

    private static void completeTask(String[] args) throws IOException {

        int taskNumber = Integer.parseInt(args[1]);
        List<String> tasks = readTasksFromFile();

        if (taskNumber >= 1 && taskNumber <= tasks.size()) {
            int index = taskNumber - 1;
            String taskToCheck = tasks.get(index);

            if (!taskToCheck.startsWith("[X] ")) {
                tasks.set(index, "[X] " + taskToCheck);
                writeAllTasksToFile(tasks);
            } else {
                System.err.println("Task " + taskNumber + " is already marked as completed.");
            }
        } else {
            System.err.println("Unable to check: index is out of bound");
        }
    }

    private static void removeTask(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Unable to remove: no index provided");
            return;
        }

        int taskNumber = Integer.parseInt(args[1]);
        List<String> tasks = readTasksFromFile();

        if (taskNumber >= 1 && tasks.size() >= taskNumber) {
            tasks.remove(taskNumber - 1);
            writeAllTasksToFile(tasks);
        } else {
            System.err.println("Unable to remove: index is out of bound");
        }
    }

    private static List<String> readTasksFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return new ArrayList<>(reader.lines().collect(Collectors.toList()));
        }
    }

    private static void writeTaskToFile(String task, boolean append) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath),
                append ? StandardOpenOption.APPEND : StandardOpenOption.WRITE)) {
            if (append && new File(filePath).length() > 0) {    // Check if file is not empty
                writer.newLine();                               // Add a newline before appending the task
            }
            writer.write(task);
        }
    }

    private static void writeAllTasksToFile(List<String> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        }
    }

    private static void printUsage() {
        System.out.println("""
                Command Line Todo application
                =============================
                
                Command line arguments:
                -l   Lists all the tasks
                -a   Adds a new task
                -r   Removes a task
                -c   Completes a task""");
    }
}