using System;
using Gtk;
using Repository;
using Controller;
using Model;
using View;

namespace View
{
    public partial class GUIStatementDialog : Dialog
    {
        private Statement statement;

        public GUIStatementDialog()
        {
            Title = "Get Statement";
            this.Build();

        }

        public Statement Statement
        {
            get
            {
                return statement;
            }

            set
            {
                statement = value;
            }
        }

        protected void OnClose(object sender, EventArgs e)
        {
            cleanup();
        }

        protected void OnDeleteEvent(object sender, EventArgs e)
        {
            cleanup();

            var args = (DeleteEventArgs)e;
            args.RetVal = true; // prevent close
        }

        protected void OnButtonOkClicked(object sender, EventArgs e)
        {
            cleanup();
        }

        protected void cleanup()
        {
            Console.WriteLine("Pressed ok");

            if (statement == null)
            {
                GUI.ShowErrorDialog(this, "Did not select any statement");
                return;
            }

            HideAll();
        }

        protected void OnButtonCompoundClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed compound");

            statement = new CompStatement(GUI.GetStatement(), GUI.GetStatement());
            label_value.Text = statement.ToString();
        }

        protected void OnButtonAssignClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed assign");


            string varname = GUI.GetString(this, "var name = ");
            statement = new AssignStatement(varname, GUI.GetExpression());
            label_value.Text = statement.ToString();
        }

        protected void OnButtonPrintClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed print");

            statement = new PrintStatement(GUI.GetExpression());
            label_value.Text = statement.ToString();
        }

        protected void OnButtonIfClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed If");

            statement = new IfStatement(GUI.GetExpression(), GUI.GetStatement(), GUI.GetStatement());
            label_value.Text = statement.ToString();
        }

        protected void OnButtonForkClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed Fork");

            statement = new ForkStatement(GUI.GetStatement());
            label_value.Text = statement.ToString();
        }

        protected void OnButtonLockEnterClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed Lock Enter");

            statement = new LockEnterStatement(GUI.GetInt(this, "lock number ="));
            label_value.Text = statement.ToString();
        }

        protected void OnButtonLockExitClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed Lock Exit");

            statement = new LockExitStatement(GUI.GetInt(this, "lock number ="));
            label_value.Text = statement.ToString();
        }
    }
}
