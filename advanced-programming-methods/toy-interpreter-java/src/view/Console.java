package view;

import controller.Interpreter;
import model.expression.*;
import model.statement.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The type Console.
 */
public class Console
{
    public static final int BACK       = 0;
    public static final int PRINT_MENU = 99;

    public static final int COMMAND_INPUT_PROGRAM         = 1;
    public static final int COMMAND_COMPLETE_EVAL         = 2;
    public static final int COMMAND_DEBUG_EVAL            = 3;
    public static final int COMMAND_INPUT_DEFAULT_PROGRAM = 4;
    public static final int COMMAND_SER_TO_FILE  = 5;
    public static final int COMMAND_SER_FROM_FILE = 6;
    public static final int COMMAND_SAVE_TO_FILE = 7;

    public static final int DEBUG_ALL    = 1;
    public static final int DEBUG_NEXT   = 2;
    public static final int DEBUG_RELOAD = 3;

    public static final int STATEMENT_COMPOUND   = 1;
    public static final int STATEMENT_ASSIGNMENT = 2;
    public static final int STATEMENT_PRINT      = 3;
    public static final int STATEMENT_IF         = 4;
    public static final int STATEMENT_FORK = 5;

    public static final int EXPRESSION_CONSTANT   = 1;
    public static final int EXPRESSION_VARIABLE   = 2;
    public static final int EXPRESSION_ARITHMETIC = 3;
    public static final int EXPRESSION_HEAP_NEW  = 4;
    public static final int EXPRESSION_HEAP_READ = 5;

    private Interpreter interpreter;
    private static Scanner keyboard = new Scanner(System.in);


    /**
     * Instantiates a new Console.
     *
     * @param interpreter the interpreter
     */
    public Console(Interpreter interpreter)
    {
        this.interpreter = interpreter;
    }

    public static Console get(Interpreter interpreter)
    {
        return new Console(interpreter);
    }

    /**
     * Run void.
     */
    public void run()
    {
        System.out.println("--- Toy interpreter ---\n");
        printMainMenu();
        testPrograms();

        while (true)
        {
            int command = getCommand("main");

            switch (command)
            {
                case COMMAND_INPUT_PROGRAM:
                    inputProgram();
                    break;

                case COMMAND_COMPLETE_EVAL:
                    completeEval();
                    break;

                case COMMAND_DEBUG_EVAL:
                    debugEval();
                    break;

                case COMMAND_INPUT_DEFAULT_PROGRAM:
                    inputDefaultProgram();
                    break;

                case COMMAND_SER_TO_FILE:
                    if (interpreter.serializeToFile())
                    {
                        System.out.println("Serialized to file.");
                    }
                    break;

                case COMMAND_SER_FROM_FILE:
                    if (interpreter.serializeFromFile())
                    {
                        System.out.println("Serialized from file.");
                        //registerCPObservers();
                    }
                    break;

                case COMMAND_SAVE_TO_FILE:
                    if (interpreter.saveToFile())
                    {
                        System.out.println("Saved to file");
                    }
                    break;

                case BACK:
                    System.out.println("Quiting...");
                    return;

                case PRINT_MENU:
                    printMainMenu();
                    break;

                default:
                    System.out.println("ERROR: Command not recognized");
            }
        }
    }

    private void registerCPObservers()
    {
        interpreter.setCPObservers(
            new StackObserver(),
            new TableObserver(),
            new OutObserver()
        );
    }

    private int getCommand(String from)
    {
        return getInt(String.format("(%s)> ", from));
    }

    public static int getInt(String ask)
    {
        String string = getString(ask);

        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            System.out.println("ERROR: The input is not an integer. Try again");
            return getInt(ask);
        }
    }

    public static String getString(String ask)
    {
        String line = null;
        while (line == null)
        {
            System.out.print(ask);
            line = keyboard.nextLine().trim();

            if (line.isEmpty())
            {
                line = null;
            }
        }

        return line;
    }

    private void printMainMenu()
    {
        String out = "";

        out += String.format("%d. Input program\n", COMMAND_INPUT_PROGRAM);
        out += String.format("%d. Run program\n", COMMAND_COMPLETE_EVAL);
        out += String.format("%d. Debug program\n", COMMAND_DEBUG_EVAL);
        out += String.format("%d. Input default program\n", COMMAND_INPUT_DEFAULT_PROGRAM);
        out += String.format("%d. Serialize to file\n", COMMAND_SER_TO_FILE);
        out += String.format("%d. Serialize from file\n", COMMAND_SER_FROM_FILE);
        out += String.format("%d. Save content to file\n", COMMAND_SAVE_TO_FILE);
        out += String.format("\n%d. Quit\n", BACK);
        out += String.format("%d. Help menu\n", PRINT_MENU);

        System.out.println(out);
    }

    private void printDebugEvalMenu()
    {
        String out = "";

        out += "Debug program:\n";
        out += String.format("%d. All(execute all the steps)\n", DEBUG_ALL);
        out += String.format("%d. Next(execute one step)\n", DEBUG_NEXT);
        out += String.format("%d. Reload(to the original state)\n", DEBUG_RELOAD);
        out += String.format("%d. Back\n", BACK);
        out += String.format("%d. Help menu\n", PRINT_MENU);

        System.out.println(out);
    }

    private void printStatementMenu()
    {
        String out = "";

        out += "Choose statement type:\n";
        out += String.format("%d. Compound\n", STATEMENT_COMPOUND);
        out += String.format("%d. Assignment \n", STATEMENT_ASSIGNMENT);
        out += String.format("%d. Print \n", STATEMENT_PRINT);
        out += String.format("%d. If \n", STATEMENT_IF);
        out += String.format("%d. Fork \n\n", STATEMENT_FORK);

        System.out.println(out);
    }

    private void printExpressionMenu()
    {
        String out = "";

        out += "Choose Expression type:\n";
        out += String.format("%d. Constant\n", EXPRESSION_CONSTANT);
        out += String.format("%d. Variable \n", EXPRESSION_VARIABLE);
        out += String.format("%d. Arithmetic \n", EXPRESSION_ARITHMETIC);
        out += String.format("%d. Heap new \n", EXPRESSION_HEAP_NEW);
        out += String.format("%d. Heap read \n\n", EXPRESSION_HEAP_READ);

        System.out.println(out);
    }

    private void printArithmeticOperatorMenu()
    {
        String out = "";

        out += "Choose arithmetic operation:\n";
        out += String.format("%d. Add\n", Operation.ADD);
        out += String.format("%d. Subtract \n", Operation.SUBTRACT);
        out += String.format("%d. Multiply \n\n", Operation.MULTIPLY);
        out += String.format("%d. Divide \n", Operation.DIVIDE);

        System.out.println(out);
    }

    private void completeEval()
    {
        System.out.println("--- Running ---");
        if (!interpreter.startMain())
        {
            System.out.println("ERROR: No program was loaded");
            return;
        }
        interpreter.completeEval();
        System.out.println("Output: \n" + interpreter.getOutput());
        System.out.println(interpreter.getRepositoryString());
        interpreter.stopMain();
    }

    private void debugEval()
    {
        printDebugEvalMenu();
        int step = 1;
        String output;
        if (!interpreter.startMain())
        {
            System.out.println("ERROR: No program was loaded. Backing up...");
            return;
        }

        while (true)
        {
            int command = getCommand("step");
            switch (command)
            {
                case DEBUG_ALL:
                    while (interpreter.debugEval())
                    {
                        output = String.format("Program State after %d step(s):\n %s \n", step, interpreter.getOutput());
                        step++;
                        System.out.println(output);
                    }

                    System.out.println("Program finished...");
                    break;

                case DEBUG_NEXT:
                    if (interpreter.debugEval())
                    {
                        output = String.format("Program State after %d step(s):\n %s \n", step, interpreter.getOutput());
                        step++;
                        System.out.println(output);
                    }
                    else
                    {
                        System.out.println("Program finished...");
                    }

                    break;

                case DEBUG_RELOAD:
                    interpreter.stopMain();
                    if (!interpreter.startMain())
                    {
                        System.out.println("ERROR: Something went wrong, reloading the program. Backing up.");
                        return;
                    }
                    step = 1;
                    System.out.println("Program reloaded");
                    break;

                case BACK:
                    System.out.println("Backing up to the main menu...");
                    interpreter.stopMain();
                    return;

                case PRINT_MENU:
                    printDebugEvalMenu();
                    break;

                default:
                    System.out.println("ERROR: Step command not recognized");
            }
        }
    }

    private void inputProgram()
    {
        System.out.println("--- Input program ---\n");

        interpreter.setMain(getStatement());
        //registerCPObservers();
    }

    private void inputDefaultProgram()
    {
//        interpreter.setCP(new CompStatement(
//            new AssignStatement("v", new ConstExpression(2)),
//            new PrintStatement(
//                new ArithmeticExpression(
//                    new VarExpression("v"),
//                    new ConstExpression(3), Operation.ADD
//                )
//            )
//        ));
//        interpreter.setMain(new CompStatement(
//            new CompStatement(
//                new AssignStatement("nr", new HeapNewExpression(new ConstExpression(355))),
//                new ForkStatement(new CompStatement(
//                    new AssignStatement("nr_fork", new ConstExpression(0)),
//                    new PrintStatement(new HeapReadExpression("nr_fork"))
//                ))
//            ),
//            new CompStatement(
//                new AssignStatement("another_nr", new ConstExpression(11)),
//                new PrintStatement(
//                    new ArithmeticExpression(
//                        new HeapReadExpression("nr"),
//                        new ConstExpression(100),
//                        Operation.SUBTRACT
//                    )
//                )
//            )
//        ));

         /*
         v=new(read());
         print(r(v));
         fork(wh(v,1+read());

         print(r(v));
         fork(wh(v,20));

         print(r(v)));
         print(r(v)+1)
        */

        interpreter.setMain(new CompStatement(
            new CompStatement(
                new AssignStatement("v", new HeapNewExpression(new ReadExpression())), // v = new(read())
                new CompStatement(
                    new PrintStatement(new HeapReadExpression("v")), // print(r(v))
                    new ForkStatement( // fork(wh(v, 1 + read());
                        new HeapWriteStatement(
                            "v",
                            new ArithmeticExpression(new ConstExpression(1), new ReadExpression(), Operation.ADD)
                        )
                    )
                )
            ),
            new CompStatement(
                new CompStatement(
                    new PrintStatement(new HeapReadExpression("v")), // print(r(v))
                    new ForkStatement( // fork(wh(v, 20));
                        new HeapWriteStatement(
                            "v",
                            new ConstExpression(20)
                        )
                    )
                ),
                new CompStatement(
                    new PrintStatement(new HeapReadExpression("v")), // print(r(v))
                    new PrintStatement( // print(r(v) + 1)
                        new ArithmeticExpression(new HeapReadExpression("v"), new ConstExpression(1), Operation.ADD)
                    )
                )
            )
        ));
        //registerCPObservers();
    }

    /**
     * Getter for property 'statement'.
     *
     * @return Value for property 'statement'.
     */
    private Statement getStatement()
    {
        printStatementMenu();
        while (true)
        {
            switch (getCommand("statement"))
            {
                case STATEMENT_COMPOUND:
                    System.out.println("CompStatement(<statement>, <statement>)");

                    return new CompStatement(getStatement(), getStatement());

                case STATEMENT_ASSIGNMENT:
                    System.out.println("Assigment expression <var> = <value>");

                    return new AssignStatement(getString("var name = "), getExpression());

                case STATEMENT_PRINT:
                    System.out.println("Print(<expression)");

                    return new PrintStatement(getExpression());

                case STATEMENT_IF:
                    System.out.println("If(<expression); then <statement>; else <statement>;");

                    return new IfStatement(getExpression(), getStatement(), getStatement());

                case STATEMENT_FORK:
                    System.out.println("fork(<statement>)");

                    return new ForkStatement(getStatement());

                default:
                    System.out.println("ERROR: Statement not recognized");
            }
        }
    }

    /**
     * Getter for property 'arithmeticOperator'.
     *
     * @return Value for property 'arithmeticOperator'.
     */
    private int getArithmeticOperator()
    {
        printArithmeticOperatorMenu();
        while (true)
        {
            int operator = getInt("operator = ");

            switch (operator)
            {
                case Operation.ADD:
                case Operation.SUBTRACT:
                case Operation.DIVIDE:
                case Operation.MULTIPLY:
                    return operator;

                default:
                    System.out.println("ERROR: operator is not valid");
            }
        }
    }

    /**
     * Getter for property 'expression'.
     *
     * @return Value for property 'expression'.
     */
    private Expression getExpression()
    {
        printExpressionMenu();
        while (true)
        {
            switch (getCommand("expression"))
            {
                case EXPRESSION_CONSTANT:
                    return new ConstExpression(getInt("const value = "));

                case EXPRESSION_VARIABLE:
                    return new VarExpression(getString("var name = "));

                case EXPRESSION_ARITHMETIC:
                    return new ArithmeticExpression(getExpression(), getExpression(), getArithmeticOperator());

                case EXPRESSION_HEAP_NEW:
                    return new HeapNewExpression(getExpression());

                case EXPRESSION_HEAP_READ:
                    return new HeapReadExpression(getString("pointer name = "));

                default:
                    System.out.println("ERROR: Expression not recognized");
            }
        }
    }

    private void testPrograms()
    {
        testProgram(new CompStatement(
            new AssignStatement("v", new ConstExpression(2)),
            new PrintStatement(
                new ArithmeticExpression(
                    new VarExpression("v"),
                    new ConstExpression(3), Operation.ADD
                )
            )
        ), "1");

        testProgram(new CompStatement(
            new AssignStatement("v", new ConstExpression(2)),
            new AssignStatement("v", new ConstExpression(155))
        ), "2");

        testProgram(new CompStatement(
            new AssignStatement("v", new ConstExpression(5)),
            new AssignStatement("v", new VarExpression("v"))
        ), "3");

        testProgram(new CompStatement(
            new AssignStatement("v", new ConstExpression(2)),
            new AssignStatement("v", new VarExpression("c"))
        ), "4");

        testProgram(new CompStatement(
            new AssignStatement("pointer", new HeapNewExpression(new ConstExpression(42))),
            new AssignStatement("pointer_value", new HeapReadExpression("pointer"))
        ), "5");

        testProgram(new CompStatement(
            new CompStatement(
                new AssignStatement("pointer", new HeapNewExpression(new ConstExpression(42))),
                new AssignStatement("foo", new ConstExpression(23))
            ),
            new AssignStatement("read", new HeapReadExpression("foo"))
        ), "6");

        testProgram(new CompStatement(
            new AssignStatement("pointer", new HeapNewExpression(new ConstExpression(42))),
            new PrintStatement(new HeapReadExpression("pointer"))
        ), "7");

        testProgram(new CompStatement(
            new CompStatement(
                new AssignStatement("nr", new ConstExpression(155)),
                new ForkStatement(new CompStatement(
                    new AssignStatement("nr_fork", new ConstExpression(23)),
                    new PrintStatement(new VarExpression("nr_fork"))
                ))
            ),
            new PrintStatement(new VarExpression("nr"))
        ), "8");

        testProgram(new CompStatement(
            new CompStatement(
                new AssignStatement("nr", new HeapNewExpression(new ConstExpression(355))),
                new ForkStatement(new CompStatement(
                    new AssignStatement("nr_fork", new ConstExpression(0)),
                    new PrintStatement(new HeapReadExpression("nr_fork"))
                ))
            ),
            new CompStatement(
                new AssignStatement("another_nr", new ConstExpression(11)),
                new PrintStatement(new HeapReadExpression("nr"))
            )
        ), "9");

        // TODO fix this
//        testProgram(new CompStatement(
//            new CompStatement(
//                new CompStatement(
//                    new LockAccessStatement(0),
//                    new AssignStatement("nr", new HeapNewExpression(new ConstExpression(100)))
//                ),
//                new ForkStatement(new CompStatement(
//                    new AssignStatement("nr_fork", new ConstExpression(0)),
//                    new CompStatement(
//                        new PrintStatement(new HeapReadExpression("nr_fork")),
//                        new LockUnlockStatement(0)
//                    )
//                ))
//            ),
//            new CompStatement(
//                new LockAccessStatement(0),
//                //new AssignStatement("another_nr", new ConstExpression(11)),
//                new PrintStatement(new VarExpression("nr"))
//            )
//        ), "10");

        testProgram(new CompStatement(
            new AssignStatement("i", new ConstExpression(5)),
            new WhileStatement(
                new VarExpression("i"),
                new CompStatement(
                    // print i
                    new PrintStatement(new VarExpression("i")),

                    // i--
                    new AssignStatement(
                        "i",
                        new ArithmeticExpression(
                            new VarExpression("i"),
                            new ConstExpression(1),
                            Operation.SUBTRACT
                        )
                    )
                )
            )
        ), "11");

        ArrayList<String> vars = new ArrayList<String>(2);
        vars.add("a");
        vars.add("b");
        ArrayList<Expression> expr = new ArrayList<Expression>(2);
        expr.add(new ConstExpression(2));
        expr.add(new ConstExpression(5));
        testProgram(new CompStatement(
            new DeclareProcStatement(
                "print_add", // print(a+b)
                vars,
                new PrintStatement(new ArithmeticExpression(new VarExpression("a"), new VarExpression("b"), Operation.ADD))
            ),
            new CompStatement(
                new CallProcStatement("print_add", expr),
                new CallProcStatement("print_add", expr)
                )
        ), "12");
    }

    private void testProgram(Statement program, String nr)
    {
        System.out.println("\n" + nr);
        interpreter.setMain(program);
        completeEval();
    }
}
