using System;
using Gtk;
using Repository;
using Controller;
using Model;
using View;

namespace View
{
    public partial class GUI : Window
    {
        private Interpreter interpreter;

        private GUI(Interpreter interpreter) : base(Gtk.WindowType.Toplevel)
        {
            this.interpreter = interpreter;
            this.Build();
        }

        public static void Run(Interpreter interpreter) 
        {
            Application.Init();
            try
            {
                var gui = new GUI(interpreter);
                gui.Show();

            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }

            Application.Run();
        }

        public static string GetString(Window parent, string message)
        {
            string text = null;
            while (text == null)
            {
                text = ShowInputDialog(parent, message);
            }

            return text;
        }

        public static int GetInt(Window parent, string message)
        {
            while (true)
            {
                string text = GetString(parent, message);

                try
                {
                    return int.Parse(text);
                }
                catch (FormatException)
                {
                    ShowErrorDialog(parent, "Input is not a number, please try again");
                }
            }
        }

        public static string ShowInputDialog(Window parent, string message)
        {
            var dialog = new MessageDialog(parent, DialogFlags.Modal | DialogFlags.DestroyWithParent, MessageType.Question, ButtonsType.Ok, null);
            dialog.Text = message;
            dialog.Title = "Input";

            var entry = new Entry();
            dialog.VBox.PackEnd(entry, true, true, 0);

            dialog.ShowAll();
            dialog.Run();
            string text = entry.Text.Trim();
            dialog.Destroy();

            if (text.Length > 0)
            {
                return text;
            }

            return null;
        }

        public static string ShowComboBoxDialog(Window parent, string message, string[] options)
        {
            var dialog = new MessageDialog(parent, DialogFlags.Modal | DialogFlags.DestroyWithParent, MessageType.Question, ButtonsType.Ok, null);
            dialog.Text = message;
            dialog.Title = "Combo";

            var combo = new ComboBox(options);
            combo.Active = 0; // make first item active
            dialog.VBox.PackEnd(combo, true, true, 0);

            dialog.ShowAll();
            dialog.Run();
            string text = combo.ActiveText;
            dialog.Destroy();

            return text;
        }

        public static void ShowErrorDialog(Window parent, string message)
        {
            var dialog = new MessageDialog(parent, DialogFlags.Modal | DialogFlags.DestroyWithParent, MessageType.Error, ButtonsType.Ok, null);
            dialog.Text = message;
            dialog.Title = "ERROR";
            dialog.Run();
            dialog.Destroy();
        }
           
        public static void ShowSuccessDialog(Window parent, string message)
        {
            var dialog = new MessageDialog(parent, DialogFlags.Modal | DialogFlags.DestroyWithParent, MessageType.Info, ButtonsType.Ok, null);
            dialog.Text = message;
            dialog.Title = "SUCCESS";
            dialog.Run();
            dialog.Destroy();
        }

        protected void OnButtonInputProgramClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed input program");
            interpreter.SetMain(GetStatement());
        }

        protected void OnButtonCompleteEvalClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed complete eval");

            if (!interpreter.StartMain())
            {
                ShowErrorDialog(this, "No program was loaded");
                return;
            }

            interpreter.CompleteEval();
            string message = "Output: \n" + interpreter.Output + "\n\n" + interpreter.GetRepositoryString();
            ShowSuccessDialog(this, message);
            interpreter.StopMain();
        }

        protected void OnButtonDebugEvalClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed debug eval");
            if (!interpreter.StartMain())
            {
                ShowErrorDialog(this, "No program was loaded");
                return;
            }

            GUIDebugDialog dialog = new GUIDebugDialog(interpreter);
        }

        protected void OnButtonInputDefaultClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed input default");
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
            ShowSuccessDialog(this, "Program added");
        }

        protected void OnButtonSerToFileClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed serialize to file");

            if (interpreter.SerializeToFile())
            {
                ShowSuccessDialog(this, "Serialized to file.");
            }
            else
            {
                ShowErrorDialog(this, "Failed to serialize to file");
            }
        }

        protected void OnButtonSerFromFileClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed serialize from file");

            if (interpreter.SerializeFromFile())
            {
                ShowSuccessDialog(this, "Serialized from file.");
            }
            else
            {
                ShowErrorDialog(this, "Failed to serialize from file");
            }
        }

        protected void OnButtonSaveToFileClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed save to file");

            if (interpreter.SaveToFile())
            {
                ShowSuccessDialog(this, "Saved to file");
            }
            else
            {
                ShowErrorDialog(this, "Failed to save to file");
            }
        }

        protected void OnButtonExitClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed exit");
            Application.Quit();
        }

        public static Statement GetStatement()
        {
            var dialog = new GUIStatementDialog();
            dialog.Run();
            Console.WriteLine("Statement run");

            var st = dialog.Statement;
            if (st == null)
            {
                Console.WriteLine("Something went wrong, statement");
            }
            dialog.Destroy();

            return st;
        }

        public static Expression GetExpression()
        {
            var dialog = new GUIExpressionDialog();
            dialog.Run();
            Console.WriteLine("Expression run");

            var expr = dialog.Expression;
            if (expr == null)
            {
                Console.WriteLine("Something went wrong, expression");
            }
            dialog.Destroy();

            return expr;
        }
    }
}
