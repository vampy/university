import controller.Interpreter;
import model.*;
import repository.MemRepository;
import view.*;
import java.util.Stack;


public class Toy
{
    public static void main(String[] args)
    {
        // Create all the stacks
        Stack<IDictionary> table = new Stack<IDictionary>();
        // Main program stack
        table.push(new JCSymTable());

        ProgramState state = new ProgramState(table, new JCOut(), new JCExeStack(), new JCHeap());
        MemRepository repository = new MemRepository(state);
        Interpreter interpreter = new Interpreter(repository);

        Console.get(interpreter).run();
//        GUI.run(interpreter);
    }
}
