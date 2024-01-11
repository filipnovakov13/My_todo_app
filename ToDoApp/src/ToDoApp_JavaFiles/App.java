package ToDoApp_JavaFiles;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) throws IOException {

        System.out.println(Arrays.toString(args));

        String[] commands = {"-l", "-a", "-c", "-r"};

        if (!Arrays.asList(commands).contains(args[0])){
            System.err.println("Unsupported argument");

            System.out.println("Command Line Todo application\n" +
                    "=============================\n" +
                    "\n" +
                    "Command line arguments:\n" +
                    "    -l   Lists all the tasks\n" +
                    "    -a   Adds a new task\n" +
                    "    -r   Removes a task\n" +
                    "    -c   Completes a task");
            return;
        }

        if (args.length == 0) {
            System.out.println("Command Line Todo application\n" +
                    "=============================\n" +
                    "\n" +
                    "Command line arguments:\n" +
                    "    -l   Lists all the tasks\n" +
                    "    -a   Adds a new task\n" +
                    "    -r   Removes a task\n" +
                    "    -c   Completes a task");
            return;
        }

        if (args[0].contentEquals("-l")) {
            try (BufferedReader reader = new BufferedReader(new FileReader("src/ToDoList/TaskList.txt"))) {
                String line;
                int lineNumber = 1;
                if (reader.readLine() != null) {
                    while ((line = reader.readLine()) != null) {
                        System.out.println(lineNumber + " - " + line);
                        lineNumber++;
                    }
                } else {
                    System.out.println("No todos for today! :)");
                }
            } catch (IOException e) {
                System.err.println("Couldn't read file!");
            }
            return;
        }

        if (args[0].contentEquals("-a")) {
            if (args.length == 1) {
                System.out.println("Unable to add: no task provided");
            } else {
                String filePath = "src/ToDoList/TaskList.txt";
                String lineToAppend = args[1];

                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.APPEND)) {
                    writer.newLine();
                    writer.write(lineToAppend);
                } catch (IOException e) {
                    System.err.println("Unable to add the task.");
                    ;
                }
            }
        }

        if (args[0].contentEquals("-c")) {
            if (args.length == 1) {
                System.out.println("Unable to check: no index provided");
            }

            try {
                int taskNumber = Integer.parseInt(args[1]);
                String filePath = "src/ToDoList/TaskList.txt";

                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                List<String> tasks = reader.lines().collect(Collectors.toList());

                if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                    int index = taskNumber - 1;
                    String taskToCheck = tasks.get(index);

                    if (!taskToCheck.startsWith("[X] ")) {
                        tasks.set(index, "[X] " + taskToCheck);

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                            for (String task : tasks) {
                                writer.write(task);
                                writer.newLine();
                            }
                        }
                    } else {
                        System.out.println("Task " + taskNumber + " is already marked as completed.");
                    }
                } else {
                    System.out.println("Unable to check: index is out of bound");
                }
            } catch (NumberFormatException e) {
                System.err.println("Unable to check: index is not a number");
            } catch (IOException e) {
                System.err.println("Error reading/writing file");
            }
        }

        if (args[0].contentEquals("-r")) {
            if (args.length < 2) {
                    System.out.println("Unable to remove: no index provided");
                throw new ArrayIndexOutOfBoundsException("");
            }

            try {
                int taskNumber = Integer.parseInt(args[1]);
                String filePath = "src/ToDoList/TaskList.txt";

                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                List<String> tasks = reader.lines().collect(Collectors.toList());

                if (taskNumber >= 1 && tasks.size() >= taskNumber) {
                    tasks.remove(taskNumber - 1);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                        for (String task : tasks) {
                            writer.write(task);
                            writer.newLine();
                        }
                    }
                } else {
                    System.out.println("Unable to remove: index is out of bound");
                }
            } catch (NumberFormatException e) {
                System.err.println("Unable to remove: index is not a number");
            } catch (IOException e) {
                System.err.println("Error reading/writing file");
            }
        }
    }
}

