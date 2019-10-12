using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace chat.client
{
    public partial class LoginWindow : Form
    {
        private ChatClientCtrl ctrl;
        public LoginWindow(ChatClientCtrl ctrl)
        {
            InitializeComponent();
            this.ctrl = ctrl;
        }

        private void clearBut_Click(object sender, EventArgs e)
        {
            userText.Clear();
            passText.Clear();
        }

        private void loginBut_Click(object sender, EventArgs e)
        {
            String user = userText.Text;
            String pass = passText.Text;
            try
            {
                ctrl.login(user, pass);
                //MessageBox.Show("Login succeded");
                ChatWindow chatWin=new ChatWindow(ctrl);
                chatWin.Text = "Chat window for " + user;
                chatWin.Show();
                this.Hide();
            }catch(Exception ex)
            {
                MessageBox.Show(this, "Login Error " + ex.Message/*+ex.StackTrace*/, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

        }

        private void LoginWindow_Load(object sender, EventArgs e)
        {

        }
    }
}
