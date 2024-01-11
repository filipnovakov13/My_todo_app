package ToDoApp_JavaFiles;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    static String filePath = "src/ToDoList/TaskList.txt";
    public static void main(String[] args) {


        if (args.length == 0) {
            printUsage();
            return;
        }

        String[] commands = {"-l", "-a", "-c", "-r"};

        if (!Arrays.asList(commands).contains(args[0])) {
            System.err.println("Unsupported argument");
            printUsage();
            return;
        }

        try {
            handleCommand(args);
        } catch (IOException e) {
            System.err.println("Error reading/writing file");
        }
    }

    private static void handleCommand(String[] args) throws IOException {
        if (args[0].equals("-l")) {
            listTasks();
        } else if (args[0].equals("-a")) {
            if (args.length < 2){
                System.out.println("Unable to add: no task provided");
                return;} else {addTask(args[1]);}
        } else if (args[0].equals("-c")) {
            if (args.length < 2){
                System.out.println("Unable to check: no task provided");
                return;} else {completeTask(args);}
        } else if (args[0].equals("-r")) {
            if (args.length < 2){
                System.out.println("Unable to remove: no task provided");
                return;} else {removeTask(args);}
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
                System.out.println("Task " + taskNumber + " is already marked as completed.");
            }
        } else {
            System.out.println("Unable to check: index is out of bound");
        }
    }

    private static void removeTask(String[] args) throws IOException {

        int taskNumber = Integer.parseInt(args[1]);
        List<String> tasks = readTasksFromFile();

        if (taskNumber >= 1 && tasks.size() >= taskNumber) {
            tasks.remove(taskNumber - 1);
            writeAllTasksToFile(tasks);
        } else {
            System.out.println("Unable to remove: index is out of bound");
        }
    }

    private static List<String> readTasksFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    private static void writeTaskToFile(String task, boolean append) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath),
                append ? StandardOpenOption.APPEND : StandardOpenOption.WRITE)) {
            writer.newLine();
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
        System.out.println("Command Line Todo application\n" +
                "=============================\n" +
                "\n" +
                "Command line arguments:\n" +
                "    -l   Lists all the tasks\n" +
                "    -a   Adds a new task\n" +
                "    -r   Removes a task\n" +
                "    -c   Completes a task");
    }
}


