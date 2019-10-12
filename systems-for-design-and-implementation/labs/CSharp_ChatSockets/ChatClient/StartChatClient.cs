using System;
using System.Collections;
using System.Collections.Generic;
using System.Windows.Forms;
using chat.services;
using chat.network.server;
using Hashtable=System.Collections.Hashtable;


namespace chat.client
{
    static class StartChatClient
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            
           
            //IChatServer server=new ChatServerMock();          
            IChatServer server = new ChatServerProxy("127.0.0.1", 55555);
            ChatClientCtrl ctrl=new ChatClientCtrl(server);
            LoginWindow win=new LoginWindow(ctrl);
            Application.Run(win);
        }
    }
}
