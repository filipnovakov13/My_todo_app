package ToDoApp_JavaFiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

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


    }
}
