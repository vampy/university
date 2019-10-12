using System;
using Repository;
using Controller;
using Model;
using View;

namespace ToyInterpreter
{
    // Console View
    class MainClass
    {
        public static void Main(string[] args)
        {
            var state = new ProgramState(new GSymTable(), new GOut(), new GExeStack(), new GHeap(), new LockTable());
            var repository = new MemRepository(state);
            var interpreter = new Interpreter(repository);

            //VConsole.Get(interpreter).Run();
            GUI.Run(interpreter);
        }
    }
}
