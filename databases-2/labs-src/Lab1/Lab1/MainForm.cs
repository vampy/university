using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Lab1
{
    public partial class MainForm : Form
    {
        // Main sql connection
        private const String ConnectionString = "Data Source=(local);Initial Catalog=leyyin;Integrated Security=SSPI;";
        private SqlConnection _connection;

        // parent fields
        private int _parentID = -1;
        private SqlDataAdapter _parentDataAdapter;
        private DataSet _parentDataSet;

        // child fields
        private int _childID = -1;
        private SqlDataAdapter _childDataAdapter;
        private DataSet _childDataSet;

        public MainForm()
        {
            InitializeComponent();
        }

        public static void MessageError(string message)
        {
            MessageBox.Show(message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Console.WriteLine("ERROR: " + message);
        }

        // display parents clicked
        private void buttonDisplayParent_Click(object sender, EventArgs e)
        {
            Console.WriteLine("Clicked buttonDisplayParent");
            if (_connection == null)
            {
                return;
            }

            try
            {
                // fill data
                _parentDataAdapter = new SqlDataAdapter("SELECT id, username, password_hash as 'password', name FROM users", _connection);
                _parentDataSet = new DataSet();
                _parentDataAdapter.Fill(_parentDataSet, "users");

                // update view
                dataGridViewParent.DataSource = _parentDataSet.Tables["users"];
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // parent row clicked
        private void dataGridViewParent_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex == -1 || _connection == null) // correct row
            {
                return;
            }

            // make defaults
            buttonAddChild.Enabled = true;
            buttonRemoveChild.Enabled = false;
            buttonUpdateChild.Enabled = false;
            textBoxID.Text = "";

            _parentID = (int)dataGridViewParent.Rows[e.RowIndex].Cells["id"].Value;
            Console.WriteLine("Clicked parent with id: {0:D}", _parentID);

            RefillDataGridChild(_parentID);
        }

        // child row clicked
        private void dataGridViewChild_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex == -1) // correct row
            {
                return;
            }

            // make buttons visible
            buttonRemoveChild.Enabled = true;
            buttonUpdateChild.Enabled = true;

            _childID = (int)dataGridViewChild.Rows[e.RowIndex].Cells["id"].Value;
            Console.WriteLine("Clicked child with id: {0:D}", _childID);

            // set text into textbox
            textBoxID.Text = _childID.ToString();
            textBoxEmail.Text = (string)dataGridViewChild.Rows[e.RowIndex].Cells["email"].Value;
        }

        // get row id for child table id
        private int GetDataGridChildRow(int id)
        {
            foreach (DataGridViewRow row in dataGridViewChild.Rows)
            {
                if (row.Cells["id"].Value.ToString().Equals(id.ToString()))
                {
                    return row.Index;
                }
            }

            return -1;
        }

        // refill the child table
        private void RefillDataGridChild(int parent_id)
        {
            try
            {
                // fill data
                SqlCommand command = new SqlCommand("SELECT id, user_id, email FROM emails WHERE user_id = @id", _connection);
                command.Parameters.AddWithValue("@id", parent_id.ToString());

                _childDataAdapter = new SqlDataAdapter(command);
                _childDataSet = new DataSet();
                _childDataAdapter.Fill(_childDataSet, "emails");

                // update view
                dataGridViewChild.DataSource = _childDataSet.Tables["emails"];
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // add email
        private void buttonAddChild_Click(object sender, EventArgs e)
        {
            Console.WriteLine("Clicked  buttonAddChild");
            if (_parentID == -1)
            {
                MessageError("Parent is negative");
                return;
            }
            if (textBoxEmail.Text.Length < 2)
            {
                MessageError("Email is too short");
                return;
            }

            try
            {
                // create new row
                DataRow row = _childDataSet.Tables["emails"].NewRow();
                row["user_id"] = _parentID;
                row["email"] = textBoxEmail.Text;
                _childDataSet.Tables["emails"].Rows.Add(row);

                // push
                new SqlCommandBuilder(_childDataAdapter);
                _childDataAdapter.Update(_childDataSet, "emails");
                RefillDataGridChild(_parentID); // refill table
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // update emails clicked
        private void buttonUpdateChild_Click(object sender, EventArgs e)
        {
            Console.WriteLine("Clicked buttonUpdateChild");
            if (_childID == -1)
            {
                MessageError("Child is negative");
                return;
            }
            if (textBoxEmail.Text.Length < 2)
            {
                MessageError("Email is too short");
                return;
            }

            try
            {
                // get row in table
                int rowIndex = GetDataGridChildRow(_childID);
                if (rowIndex == -1)
                {
                    MessageError("Row index is negative");
                    return;
                }

                // update row 
                DataRow row = _childDataSet.Tables["emails"].Rows[rowIndex];
                row["email"] = textBoxEmail.Text;

                // push
                new SqlCommandBuilder(_childDataAdapter);
                _childDataAdapter.Update(_childDataSet, "emails");
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // remove email
        private void buttonRemoveChild_Click(object sender, EventArgs e)
        {
            Console.WriteLine("Clicked buttonRemoveChild");
            if (_childID == -1 || _parentID == -1)
            {
                MessageError("Child or parent is negative");
                return;
            }

            try
            {
                // get row in table
                int rowIndex = GetDataGridChildRow(_childID);
                if (rowIndex == -1)
                {
                    MessageError("Row index is negative");
                    return;
                }

                // remove
                _childDataSet.Tables["emails"].Rows[rowIndex].Delete();

                // push
                new SqlCommandBuilder(_childDataAdapter);
                _childDataAdapter.Update(_childDataSet, "emails");
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // form loading
        private void MainForm_Load(object sender, EventArgs e)
        {
            try
            {
                _connection = new SqlConnection(ConnectionString);
                _connection.Open();
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // form closing
        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (_connection != null)
            {
                try
                {
                    _connection.Close();
                }
                catch (SqlException ex)
                {
                    MessageError(ex.Message);
                }
            }
        }
    }
}
