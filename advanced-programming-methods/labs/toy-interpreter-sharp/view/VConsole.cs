using System;
using Model;
using Controller;
using Exceptions;

namespace View
{
    /// <summary>
    /// The type Gui.
    /// </summary>
    public class VConsole
    {
        private Interpreter interpreter;
    
        public const int BACK = 0;
        public const int PRINT_MENU = 99;

        public const int COMMAND_INPUT_PROGRAM = 1;
        public const int COMMAND_COMPLETE_EVAL = 2;
        public const int COMMAND_DEBUG_EVAL = 3;
        public const int COMMAND_INPUT_DEFAULT_PROGRAM = 4;

        public const int COMMAND_SER_TO_FILE = 5;
        public const int COMMAND_SER_FROM_FILE = 6;
        public const int COMMAND_SAVE_TO_FILE = 7;

        public const int DEBUG_ALL = 1;
        public const int DEBUG_NEXT = 2;
        public const int DEBUG_RELOAD = 3;

        public const int STATEMENT_COMPOUND = 1;
        public const int STATEMENT_ASSIGNMENT = 2;
        public const int STATEMENT_PRINT = 3;
        public const int STATEMENT_IF = 4;
        public const int STATEMENT_FORK = 5;

        public const int EXPRESSION_CONSTANT = 1;
        public const int EXPRESSION_VARIABLE = 2;
        public const int EXPRESSION_ARITHMETIC = 3;
        public const int EXPRESSION_HEAP_NEW = 4;
        public const int EXPRESSION_HEAP_READ = 5;

        /// <summary>
        /// Instantiates a new Gui.
        /// </summary>
        /// <param name="interpreter"> the interpreter </param>
        public VConsole(Interpreter interpreter)
        {
            this.interpreter = interpreter;
        }

        public static VConsole Get(Interpreter interpreter)
        {
            return new VConsole(interpreter);
        }

        /// <summary> Run void. </summary>
        public virtual void Run()
        {
            Console.WriteLine("--- Toy interpreter ---\n");
            PrintMainMenu();
            TestPrograms();

            while (true)
            {
                int command = GetCommand("main");

                switch (command)
                {
                    case COMMAND_INPUT_PROGRAM:
                        InputProgram();
                        break;

                    case COMMAND_COMPLETE_EVAL:
                        CompleteEval();
                        break;

                    case COMMAND_DEBUG_EVAL:
                        DebugEval();
                        break;

                    case COMMAND_INPUT_DEFAULT_PROGRAM:
                        InputDefaultProgram();
                        break;

                    case COMMAND_SER_TO_FILE:
                        if (interpreter.SerializeToFile())
                        {
                            Console.WriteLine("Serialized to file.");
                        }
                        break;

                    case COMMAND_SER_FROM_FILE:
                        if (interpreter.SerializeFromFile())
                        {
                            Console.WriteLine("Serialized from file.");
                            //RegisterCPObservers();
                        }
                        break;

                    case COMMAND_SAVE_TO_FILE:
                        if (interpreter.SaveToFile())
                        {
                            Console.WriteLine("Saved to file");
                        }
                        break;

                    case BACK:
                        Console.WriteLine("Quiting...");
                        return;

                    case PRINT_MENU:
                        PrintMainMenu();
                        break;

                    default:
                        Console.WriteLine("ERROR: Command not recognized");
                        break;
                }
            }
        }

        private void RegisterCPObservers()
        {
            interpreter.SetCPObservers(new StackObserver(), new TableObserver(), new OutObserver());
        }

        private int GetCommand(string from)
        {
            return GetInt(string.Format("({0})> ", from));
        }

        private int GetInt(string ask)
        {
            // TODO use while loop
            Console.Write(ask);

            try
            {
                return int.Parse(Console.ReadLine());
            }
            catch (FormatException)
            {
                Console.WriteLine("ERROR: The input is not an integer. TRY AGAIN");
                return GetInt(ask);
            }
        }

        private static string GetString(string ask)
        {
            Console.Write(ask);

            return Console.ReadLine();
        }

        private static void PrintMainMenu()
        {
            string @out = "";

            @out += string.Format("{0:D}. Input program\n", COMMAND_INPUT_PROGRAM);
            @out += string.Format("{0:D}. Run program\n", COMMAND_COMPLETE_EVAL);
            @out += string.Format("{0:D}. Debug program\n", COMMAND_DEBUG_EVAL);
            @out += string.Format("{0:D}. Input default program\n", COMMAND_INPUT_DEFAULT_PROGRAM);
            @out += string.Format("{0:D}. Serialize to file\n", COMMAND_SER_TO_FILE);
            @out += string.Format("{0:D}. Serialize from file\n", COMMAND_SER_FROM_FILE);
            @out += string.Format("{0:D}. Save content to file\n", COMMAND_SAVE_TO_FILE);
            @out += string.Format("\n{0:D}. Quit\n", BACK);
            @out += string.Format("{0:D}. Help menu\n", PRINT_MENU);

            Console.WriteLine(@out);
        }

        private static void PrintDebugEvalMenu()
        {
            string @out = "";

            @out += "Debug program:\n";
            @out += string.Format("{0:D}. All(execute all the steps)\n", DEBUG_ALL);
            @out += string.Format("{0:D}. Next(execute one step)\n", DEBUG_NEXT);
            @out += string.Format("{0:D}. Reload(to the original state)\n", DEBUG_RELOAD);
            @out += string.Format("{0:D}. Back\n", BACK);
            @out += string.Format("{0:D}. Help menu\n", PRINT_MENU);

            Console.WriteLine(@out);
        }

        private static void PrintStatementMenu()
        {
            string @out = "";

            @out += "Choose statement type:\n";
            @out += string.Format("{0:D}. Compound\n", STATEMENT_COMPOUND);
            @out += string.Format("{0:D}. Assignment \n", STATEMENT_ASSIGNMENT);
            @out += string.Format("{0:D}. Print \n", STATEMENT_PRINT);
            @out += string.Format("{0:D}. If \n", STATEMENT_IF);
            @out += string.Format("{0:D}. Fork \n\n", STATEMENT_FORK);

            Console.WriteLine(@out);
        }

        private static void PrintExpressionMenu()
        {
            string @out = "";

            @out += "Choose Expression type:\n";
            @out += string.Format("{0:D}. Constant\n", EXPRESSION_CONSTANT);
            @out += string.Format("{0:D}. Variable \n", EXPRESSION_VARIABLE);
            @out += string.Format("{0:D}. Arithmetic \n", EXPRESSION_ARITHMETIC);
            @out += string.Format("{0:D}. Heap new \n", EXPRESSION_HEAP_NEW);
            @out += string.Format("{0:D}. Heap read \n\n", EXPRESSION_HEAP_READ);

            Console.WriteLine(@out);
        }

        private static void PrintArithmeticOperatorMenu()
        {
            string @out = "";

            @out += "Choose arithmetic operation:\n";
            @out += string.Format("{0:D}. Add\n", Operation.ADD);
            @out += string.Format("{0:D}. Subtract \n", Operation.SUBTRACT);
            @out += string.Format("{0:D}. Divide \n", Operation.DIVIDE);
            @out += string.Format("{0:D}. Multiply \n\n", Operation.MULTIPLY);

            Console.WriteLine(@out);
        }

        private void CompleteEval()
        {
            Console.WriteLine("--- Running ---");
            if (!interpreter.StartMain())
            {
                Console.WriteLine("ERROR: No program was loaded");
                return;
            }
            interpreter.CompleteEval();
            Console.WriteLine("Output: \n" + interpreter.Output);
            Console.WriteLine(interpreter.GetRepositoryString());
            interpreter.StopMain();;
        }

        private void DebugEval()
        {
            PrintDebugEvalMenu();
            int step = 1;
            string output;
            if (!interpreter.StartMain())
            {
                Console.WriteLine("ERROR: No program was loaded. Backing up...");
                return;
            }

            while (true)
            {
                int command = GetCommand("step");
                switch (command)
                {
                    case DEBUG_ALL:
                        while (interpreter.DebugEval())
                        {
                            output = string.Format("Program State after {0:D} step(s):\n {1} \n", step, interpreter.Output);
                            step++;
                            Console.WriteLine(output);
                        }

                        Console.WriteLine("Program finished...");
                        break;

                    case DEBUG_NEXT:
                        if (interpreter.DebugEval())
                        {
                            output = string.Format("Program State after {0:D} step(s):\n {1} \n", step, interpreter.Output);
                            step++;
                            Console.WriteLine(output);
                        }
                        else
                        {
                            Console.WriteLine("Program finished...");
                        }

                        break;

                    case DEBUG_RELOAD:
                        interpreter.StopMain();
                        if (!interpreter.StartMain())
                        {
                            Console.WriteLine("ERROR: Something went wrong, reloading the program. Backing up.");
                            return;
                        }
                        step = 1;
                        Console.WriteLine("Program reloaded");
                        break;

                    case BACK:
                        Console.WriteLine("Backing up to the main menu...");
                        interpreter.StopMain();
                        return;

                    case PRINT_MENU:
                        PrintDebugEvalMenu();
                        break;

                    default:
                        Console.WriteLine("ERROR: Step command not recognized");
                        break;
                }
            }
        }

        private void InputProgram()
        {
            Console.WriteLine("--- Input program ---\n");

            interpreter.SetMain(Statement);
            //RegisterCPObservers();
        }

        private void InputDefaultProgram()
        {
        /*
            interpreter.SetMain(new CompStatement(
                    new CompStatement(
                        new AssignStatement("nr", new HeapNewExpression(355)),
                        new ForkStatement(new CompStatement(
                                new AssignStatement("nr_fork", new ConstExpression(0)),
                                new PrintStatement(new HeapReadExpression("nr_fork"))
                            ))
                    ),
                    new CompStatement(
                        new AssignStatement("another_nr", new ConstExpression(11)),
                        new PrintStatement(
                            new ArithmeticExpression(
                                new HeapReadExpression("nr"),
                                new ConstExpression(100),
                                Operation.SUBTRACT
                            )
                        )
                    )
                ));
                */
            /*
            v=10;
            lockEnter(1);
            print(v);
            fork(v=20;lockEnter(1);print(v);lockExit(1));
            print(v+1);
            lockExit(1)

            */

            interpreter.SetMain(new CompStatement(
                new CompStatement(
                    new AssignStatement("v", new ConstExpression(10)), // v = 10
                    new CompStatement( // lockEnter(1), print(v)
                        new LockEnterStatement(1),
                        new PrintStatement(new VarExpression("v"))
                    )
                ),
                new CompStatement(
                    new ForkStatement( // fork()
                        new CompStatement(
                            new CompStatement( // 
                                new AssignStatement("v", new ConstExpression(20)),
                                new LockEnterStatement(1)
                            ),
                            new CompStatement(
                                new PrintStatement(new VarExpression("v")),
                                new LockExitStatement(1)

                            )
                        )
                    ),
                    new CompStatement( // print(v+1); lockExit(1)
                        new PrintStatement(new ArithmeticExpression(new VarExpression("v"), new ConstExpression(1), Operation.ADD)),
                        new LockExitStatement(1)
                    )
                )
            ));
            //RegisterCPObservers();
        }

        /// <summary>
        /// Getter for property 'statement'.
        /// </summary>
        /// <returns> Value for property 'statement'. </returns>
        private Statement Statement
        {
            get
            {
                PrintStatementMenu();
                while (true)
                {
                    switch (GetCommand("statement"))
                    {
                        case STATEMENT_COMPOUND:
                            Console.WriteLine("CompStatement(<statement>, <statement>)");

                            return new CompStatement(Statement, Statement);

                        case STATEMENT_ASSIGNMENT:
                            Console.WriteLine("Assigment expression <var> = <value>");

                            return new AssignStatement(GetString("var name = "), Expression);

                        case STATEMENT_PRINT:
                            Console.WriteLine("Print(<expression)");

                            return new PrintStatement(Expression);

                        case STATEMENT_IF:
                            Console.WriteLine("If(<expression); then <statement>; else <statement>;");

                            return new IfStatement(Expression, Statement, Statement);

                        case STATEMENT_FORK:
                            Console.WriteLine("fork(<statement>");

                            return new ForkStatement(Statement);

                        default:
                            Console.WriteLine("ERROR: Statement not recognized");
                            break;
                    }
                }
            }
        }

        /// <summary>
        /// Getter for property 'arithmeticOperator'.
        /// </summary>
        /// <returns> Value for property 'arithmeticOperator'. </returns>
        private int ArithmeticOperator
        {
            get
            {
                PrintArithmeticOperatorMenu();
                while (true)
                {
                    int @operator = GetInt("operator = ");

                    switch (@operator)
                    {
                        case Operation.ADD:
                        case Operation.SUBTRACT:
                        case Operation.DIVIDE:
                        case Operation.MULTIPLY:
                            return @operator;

                        default:
                            Console.WriteLine("ERROR: operator is not valid");
                            break;
                    }
                }
            }
        }

        /// <summary>
        /// Getter for property 'expression'.
        /// </summary>
        /// <returns> Value for property 'expression'. </returns>
        private Expression Expression
        {
            get
            {
                PrintExpressionMenu();
                while (true)
                {
                    switch (GetCommand("expression"))
                    {
                        case EXPRESSION_CONSTANT:
                            return new ConstExpression(GetInt("const value = "));

                        case EXPRESSION_VARIABLE:
                            return new VarExpression(GetString("var name = "));

                        case EXPRESSION_ARITHMETIC:
                            return new ArithmeticExpression(Expression, Expression, ArithmeticOperator);
                        
                        case EXPRESSION_HEAP_NEW:
                            return new HeapNewExpression(GetInt("value on heap = "));

                        case EXPRESSION_HEAP_READ:
                            return new HeapReadExpression(GetString("pointer name = "));

                        default:
                            Console.WriteLine("ERROR: Expression not recognized");
                            break;
                    }
                }
            }
        }

        private void TestPrograms()
        {
            TestProgram(new CompStatement(
                    new AssignStatement("v", new ConstExpression(2)),
                    new PrintStatement(
                        new ArithmeticExpression(
                            new VarExpression("v"),
                            new ConstExpression(3), Operation.ADD
                        )
                    )
                ), "1");

            TestProgram(new CompStatement(
                    new AssignStatement("v", new ConstExpression(2)),
                    new AssignStatement("v", new ConstExpression(155))
                ), "2");

            TestProgram(new CompStatement(
                    new AssignStatement("v", new ConstExpression(5)),
                    new AssignStatement("v", new VarExpression("v"))
                ), "3");

            TestProgram(new CompStatement(
                    new AssignStatement("v", new ConstExpression(2)),
                    new AssignStatement("v", new VarExpression("c"))
                ), "4");

            TestProgram(new CompStatement(
                    new AssignStatement("pointer", new HeapNewExpression(42)),
                    new AssignStatement("pointer_value", new HeapReadExpression("pointer"))
                ), "5");

            TestProgram(new CompStatement(
                    new CompStatement(
                        new AssignStatement("pointer", new HeapNewExpression(42)),
                        new AssignStatement("foo", new ConstExpression(23))
                    ),
                    new AssignStatement("read", new HeapReadExpression("foo"))
                ), "6");

            TestProgram(new CompStatement(
                    new AssignStatement("pointer", new HeapNewExpression(42)),
                    new PrintStatement(new HeapReadExpression("pointer"))
                ), "7");

            TestProgram(new CompStatement(
                    new CompStatement(
                        new AssignStatement("nr", new ConstExpression(155)),
                        new ForkStatement(new CompStatement(
                                new AssignStatement("nr_fork", new ConstExpression(23)),
                                new PrintStatement(new VarExpression("nr_fork"))
                            ))
                    ),
                    new PrintStatement(new VarExpression("nr"))
                ), "8");

            TestProgram(new CompStatement(
                    new CompStatement(
                        new AssignStatement("nr", new HeapNewExpression(355)),
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
        }

        private void TestProgram(Statement program, string nr)
        {
            Console.WriteLine("\n" + nr);
            interpreter.SetMain(program);
            CompleteEval();
        }
    }
}    