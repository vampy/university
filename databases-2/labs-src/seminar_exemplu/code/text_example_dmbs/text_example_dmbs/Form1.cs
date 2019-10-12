using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Data.SqlClient; 

namespace text_example_dmbs
{
    public partial class Form1 : Form
    {
        string connString;
        SqlConnection dbConn;
        SqlDataAdapter daUsers, daPosts;
        DataSet ds;
        BindingSource bsUsers, bsPosts;
        SqlCommandBuilder cbPosts;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            connString = "Data Source = GABRIEL\\SQLEXPRESS ; Initial Catalog = test_example_dbms ; Integrated Security=SSPI";
            dbConn = new SqlConnection(connString);

            daUsers = new SqlDataAdapter("SELECT * FROM Users", dbConn);
            daPosts = new SqlDataAdapter("SELECT * FROM Posts", dbConn);
            cbPosts = new SqlCommandBuilder(daPosts);

            ds = new DataSet();
            getData();

            ds.Relations.Add(new DataRelation("FK_Posts_Users", ds.Tables["Users"].Columns["uid"],
                ds.Tables["Posts"].Columns["uid"]));

            bsUsers = new BindingSource();
            bsUsers.DataSource = ds;
            bsUsers.DataMember = "Users";

            bsPosts = new BindingSource();
            bsPosts.DataSource = bsUsers;
            bsPosts.DataMember = "FK_Posts_Users";

            cbxUsers.DataSource = bsUsers;
            cbxUsers.DisplayMember = "name";

            dgvPosts.DataSource = bsPosts;

        }

        private void getData()
        {
            ds.Clear();
            daUsers.Fill(ds, "Users");
            daPosts.Fill(ds, "Posts");
        }
        // get data button
        private void button1_Click(object sender, EventArgs e)
        {
            getData();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            daPosts.Update(ds, "Posts");

        }


      
    }
}
