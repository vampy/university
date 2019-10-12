using System;
using Gtk;
using Repository;
using Controller;
using System.IO;
using Model;
using View;

namespace View
{
    public partial class GUIDebugDialog : Dialog
    {
        private Interpreter interpreter;
        public static int step = 1;

        public GUIDebugDialog(Interpreter interpreter)
        {
            this.interpreter = interpreter;
            this.Build();
            Title = "Debug Eval";
        }

        protected void OnClose(object sender, EventArgs e)
        {
            cleanup();
        }

        protected void OnDeleteEvent(object sender, EventArgs e)
        {
            cleanup();
        }

        protected void OnButtonCancelClicked(object sender, EventArgs e)
        {
            cleanup();
        }

        protected void cleanup()
        {
            Console.WriteLine("Pressed debug cancel");
        
            interpreter.StopMain();
            Destroy();
        }

        protected void OnButtonAllClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed debug all");

            int step = 1;
            string output = "";
            while (interpreter.DebugEval())
            {
                output += string.Format("Program State after {0} step(s):\n {1} \n\n", step, interpreter.Output);
                step++;
            }
            if (step > 1)
            {
                text_output.Buffer.Text = output;
            }

            GUI.ShowSuccessDialog(this, "Program finished...");
            SaveToFile("debug.txt");
        }

        protected void OnButtonNextClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed debug next");

            if (interpreter.DebugEval())
            {
                string output = string.Format("Program State after {0} step(s):\n {1} \n", step, interpreter.Output);
                step++;
                text_output.Buffer.Text = text_output.Buffer.Text + "\n" + output;
            }
            else
            {
                GUI.ShowSuccessDialog(this, "Program finished...");
                step = 1;
                SaveToFile("debug.txt");
            }
        }

        protected void OnButtonReloadClicked(object sender, EventArgs e)
        {
            Console.WriteLine("Pressed debug reload");

            interpreter.StopMain();
            if (!interpreter.StartMain())
            {
                GUI.ShowErrorDialog(this, "Something went wrong, reloading the program.");
                return;
            }
            step = 1;
            GUI.ShowSuccessDialog(this, "Program reloaded...");
            text_output.Buffer.Text = "";
        }

        // scroll to bottom
        protected void OnTextOutputSizeAllocated(object o, SizeAllocatedArgs args)
        {
            text_output.ScrollToIter(text_output.Buffer.EndIter, 0, false, 0, 0);
        }

        protected void SaveToFile(string filename)
        {
            try
            {
                StreamWriter writer = new StreamWriter(filename);
                writer.WriteLine(text_output.Buffer.Text);
                writer.Close();
            }
            catch (IOException e)
            {
                Console.WriteLine("ERROR: could not save: " + e.Message);
            }
        }
    }
}
