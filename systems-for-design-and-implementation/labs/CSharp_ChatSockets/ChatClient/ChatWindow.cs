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
    
    public partial class ChatWindow : Form
    {
       
        private readonly ChatClientCtrl ctrl;
        private readonly IList<String> friendsData;
        private readonly IList<String> messageData;
        public ChatWindow(ChatClientCtrl ctrl)
        {
            InitializeComponent();
            this.ctrl = ctrl;
            friendsData = ctrl.getLoggedFriends();
            messageData=new List<string>();
            friendList.DataSource = friendsData;
            
            messageList.DataSource = messageData;
            ctrl.updateEvent += userUpdate;
        }

        


        private void ChatWindow_FormClosing(object sender, FormClosingEventArgs e)
        {
            Console.WriteLine("ChatWindow closing "+e.CloseReason);
            if (e.CloseReason == CloseReason.UserClosing)
            {
                ctrl.logout();
                ctrl.updateEvent -= userUpdate;
				Application.Exit();
            }
          
        }

        public void userUpdate(object sender, ChatUserEventArgs e)
        {
            if (e.UserEventType==ChatUserEvent.FriendLoggedIn)
            {
                String friendId = e.Data.ToString();
                friendsData.Add(friendId);
                Console.WriteLine("[ChatWindow] friendLoggedIn "+ friendId);
                friendList.BeginInvoke(new UpdateListBoxCallback(this.updateListBox), new Object[]{friendList, friendsData});
            }
            if (e.UserEventType == ChatUserEvent.FriendLoggedOut)
            {
                String friendId = e.Data.ToString();
                friendsData.Remove(friendId);
                Console.WriteLine("[ChatWindow] friendLoggedOut " + friendId);
                friendList.BeginInvoke(new UpdateListBoxCallback(this.updateListBox), new Object[] { friendList, friendsData });
            }
            if (e.UserEventType == ChatUserEvent.NewMessage)
            {
                String messString = e.Data.ToString();
                messageData.Add(messString);
               Console.WriteLine("[ChatWindow] messString " + messString);
                messageList.BeginInvoke(new UpdateListBoxCallback(this.updateListBox), new Object[] { messageList, messageData});
            }
        }
        //for updating the GUI

        //1. define a method for updating the ListBox
        private void updateListBox(ListBox listBox, IList<String> newData)
        {
            listBox.DataSource = null;
            listBox.DataSource = newData;
        }

        //2. define a delegate to be called back by the GUI Thread
        public delegate void UpdateListBoxCallback(ListBox list, IList<String> data);

        

        //3. in the other thread call like this:
        /*
         * list.Invoke(new UpdateListBoxCallback(this.updateListBox), new Object[]{list, data});
         * 
         * */

        private void sendBut_Click(object sender, EventArgs e)
        {
            String messTxt = messText.Text;
            if (messTxt.Trim()=="")
            {
                MessageBox.Show(this, "You must introduce some text", "Error", MessageBoxButtons.OK,
                                MessageBoxIcon.Error);
                return;
            }
            int indexFr = friendList.SelectedIndex;
            if (indexFr < 0)
            {
                MessageBox.Show(this, "You must select a friend", "Error", MessageBoxButtons.OK,
                                MessageBoxIcon.Error);
                return;
            }
            String friendId = friendsData[indexFr];
            ctrl.sendMessage(friendId, messTxt);
            messText.Clear();
            
        }

        private void ChatWindow_Load(object sender, EventArgs e)
        {

        }


    }
}
