package Utils;

import java.util.LinkedList;
import java.util.List;
import views.ConsoleOutput;

/**
 *
 * @author Michal
 */
public class Logger {
    
   private List<ConsoleOutput> console;

    public Logger() {
        console = new LinkedList<>();
    }

   public void log(String message) {
        for (ConsoleOutput output : console) {
            output.println(message);
        }
        System.out.println(message);
    }
    
    
    public void addConsoleOutput(ConsoleOutput output) {
        console.add(output);
    }

    public void removeConsoleOutputs() {
        console.clear();
    }

    public void removeConsoleOutput(ConsoleOutput output) {
        console.remove(output);
    }
}
