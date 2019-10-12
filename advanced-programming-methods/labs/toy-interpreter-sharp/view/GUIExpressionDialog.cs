using System;
using Gtk;
using Repository;
using Controller;
using Model;
using View;

namespace View
{
    public partial class GUIExpressionDialog : Gtk.Dialog
    {
        private Expression expression;

        public GUIExpressionDialog()
        {
            Title = "Get Expression";
            this.Build();
        }

        public Expression Expression
        {
            get
            {
                return expression;
            }

            set
            {
                expression = value;
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

            if (expression == null)
            {
                GUI.ShowErrorDialog(this, "Did not select any statement");
                return;
            }

            HideAll();
        }

        protected void OnButtonConstClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed const");

            expression = new ConstExpression(GUI.GetInt(this, "const value = "));
            label_value.Text = expression.ToString();
        }

        protected void OnButtonVarClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed var");

            expression = new VarExpression(GUI.GetString(this, "var name = "));
            label_value.Text = expression.ToString();
        }

        protected void OnButtonArhitmetic1Clicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed arithmetic");

            var left = GUI.GetExpression();

            string[] selectionValues = {"Add", "Subtract", "Divide", "Multiply"};
            string selection = GUI.ShowComboBoxDialog(this, "Type of operation?", selectionValues);
            int op = Operation.ADD;
            if (selection.Equals("Add"))
            {
                op = Operation.ADD;
            }
            else if (selection.Equals("Subtract"))
            {
                op = Operation.SUBTRACT;
            }
            else if (selection.Equals("Divide"))
            {
                op = Operation.DIVIDE;
            }
            else if (selection.Equals("Multiply"))
            {
                op = Operation.MULTIPLY;
            }
            else
            {
                Console.WriteLine("ERROR: No operator matches. This should never happen");
            }

            var right = GUI.GetExpression();

            expression = new ArithmeticExpression(left, right, op);
            label_value.Text = expression.ToString();
        }

        protected void OnButtonHeapNewClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed heap new");

            expression = new HeapNewExpression(GUI.GetInt(this, "value on heap = "));
            label_value.Text = expression.ToString();
        }

        protected void OnButtonHeapReadClicked(object sender, EventArgs e)
        {                
            Console.WriteLine("Pressed heap read");

            expression = new HeapReadExpression(GUI.GetString(this, "pointer name = "));
            label_value.Text = expression.ToString();
        }
    }
}
