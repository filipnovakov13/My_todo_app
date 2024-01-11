package ToDoApp_JavaFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class App {

    public static void main(String[] args) throws IOException {

        System.out.println(Arrays.toString(args));

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
            try(BufferedReader reader = new BufferedReader(new FileReader("src/ToDoList/TaskList.txt"))) {
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
                    writer.write(lineToAppend);
                    writer.newLine();
                } catch (IOException e) {
                    System.err.println("Unable to add the task.");;
                }
            }
        }

        if (args[0].contentEquals("-c")) {
            if (args.length == 1) {
                System.out.println("Unable to check: no index provided");
            }
        }


    }
}
