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
using System.Configuration;
using System.Diagnostics;

namespace Labs
{
    public partial class MainForm : Form
    {
        // Main sql connection
        private string _connectionString;
        private SqlConnection _connection;

        // parent fields
        private string _parentTable, _parentIDName, _parentSelectColumns;
        private int _parentID = -1;
        private SqlDataAdapter _parentDataAdapter;
        private DataSet _parentDataSet = new DataSet();

        // child fields
        private string _childTable, _childIDName, _childParentIDName, _childSelectColumns;
        private int _childID = -1;
        private SqlDataAdapter _childDataAdapter;
        private DataSet _childDataSet = new DataSet();
        private Dictionary<string, ChildColumn> _childColumns = new Dictionary<string, ChildColumn>(); // map from column name => ChildColumn

        public static string ConvertFromDBVal(object obj)
        {
            if (obj == null || obj == DBNull.Value)
            {
                return default(string); // returns the default value for the type
            }
            else
            {
                return obj.ToString();
            }
        }

        public MainForm()
        {
            InitializeComponent();
            InitFromConfig();
        }

        private void InitFromConfig()
        {
            // get settings
            _connectionString = ConfigurationManager.AppSettings["db_connection_string"];
            _childTable = ConfigurationManager.AppSettings["child_table"];
            _childIDName = ConfigurationManager.AppSettings["child_id_name"];
            _childParentIDName = ConfigurationManager.AppSettings["child_parent_id_name"];
            _childSelectColumns = ConfigurationManager.AppSettings["child_select_columns"].Trim();
            _parentTable = ConfigurationManager.AppSettings["parent_table"];
            _parentIDName = ConfigurationManager.AppSettings["parent_id_name"];
            _parentSelectColumns = ConfigurationManager.AppSettings["parent_select_columns"].Trim();

            // add custom forms for child table from the config file
            string editColumns = ConfigurationManager.AppSettings["child_edit_columns"];
            string[] columns = editColumns.Split(';');
            foreach (string column in columns)
            {
                // ignore empty entries
                if (column.Trim().Length == 0)
                {
                    continue;
                }

                string[] temp_fields = column.Split(','); // may hold Label=columnName,__type=int
                string[] temp_label_name;
                SqlDbType dbType = SqlDbType.VarChar;

                if (temp_fields.Length > 1) // found additional info 
                {
                    temp_label_name = temp_fields[0].Split('='); // get label => column name
                    

                    // get db info
                    string[] str_db_type = temp_fields[1].Split('=');
                    Debug.Assert(str_db_type.Length == 2);

                    string option = str_db_type[0].ToLower(),
                        value = str_db_type[1].ToLower();

                    // set for option
                    switch (option)
                    {
                        case "__type":
                            switch (value)
                            {
                                case "int":
                                    dbType = SqlDbType.Int;
                                    break;
                                case "varchar":
                                    dbType = SqlDbType.VarChar;
                                    break;
                                case "float":
                                    dbType = SqlDbType.Float;
                                    break;
                                case "bit":
                                    dbType = SqlDbType.Bit;
                                    break;
                                case "timestamp":
                                    dbType = SqlDbType.Timestamp;
                                    break;
                                case "time":
                                    dbType = SqlDbType.Time;
                                    break;
                                case "date":
                                    dbType = SqlDbType.Date;
                                    break;
                                case "datetime":
                                    dbType = SqlDbType.DateTime;
                                    break;
                                default:
                                    throw new Exception(string.Format("Invalid type = '{0}' for __type in config file", value));
                            }
                            break;

                        default:
                            throw new Exception(string.Format("Invalid option = '{0}' from config file", option));
                    }
                }
                else // no aditional info
                {
                    temp_label_name = column.Split('='); // get label => column name
                }


                string label = temp_label_name[0],
                       name = temp_label_name[1];

                // add to GUI
                Console.WriteLine("Label = '{0}', Name = '{1}', Type = {2}", label, name, dbType);
                var textbox = new TextBox() { Dock = DockStyle.Fill };
                tableLayoutChild.Controls.Add(new Label() { Text = label, Anchor = AnchorStyles.Top, AutoSize = true });
                tableLayoutChild.Controls.Add(textbox);
                //tableLayoutChild.RowStyles[tableLayoutChild.RowCount - 1].SizeType = SizeType.Percent;

                // add to our global dictionary
                _childColumns[name] = new ChildColumn() { Name = name, Textbox = textbox, DbType = dbType };
            }

            // see if settings are there
            Debug.Assert(_connectionString.Length > 0);
            Debug.Assert(editColumns.Length > 0);
            Debug.Assert(_childTable.Length > 0);
            Debug.Assert(_childIDName.Length > 0);
            Debug.Assert(_childParentIDName.Length > 0);
            Debug.Assert(_parentTable.Length > 0);
            Debug.Assert(_parentIDName.Length > 0);
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
                // select columns
                string columns = "*"; // default, all
                if (_parentSelectColumns.Length > 0) // custom select
                {
                    columns = _parentIDName + ", " + _parentSelectColumns;
                }

                _parentDataAdapter = new SqlDataAdapter("SELECT " + columns + " FROM " + _parentTable, _connection);
                _parentDataSet.Clear();
                _parentDataAdapter.Fill(_parentDataSet, _parentTable);  // fill data

                // update view
                dataGridViewParent.DataSource = _parentDataSet.Tables[_parentTable];
                dataGridViewChild.DataSource = null;
                buttonRemoveChild.Enabled = false;
                buttonUpdateChild.Enabled = false;
                buttonAddChild.Enabled = false;
                textBoxID.Text = "";
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

            _parentID = (int)dataGridViewParent.Rows[e.RowIndex].Cells[_parentIDName].Value;
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

            _childID = (int)dataGridViewChild.Rows[e.RowIndex].Cells[_childIDName].Value;
            Console.WriteLine("Clicked child with id: {0:D}", _childID);

            // set text into textbox
            textBoxID.Text = _childID.ToString();
            foreach (KeyValuePair<string, ChildColumn> entry in _childColumns) // update all our forms
            {
                entry.Value.Textbox.Text = ConvertFromDBVal(dataGridViewChild.Rows[e.RowIndex].Cells[entry.Key].Value);
            }
        }

        // get row id for child table id
        private int GetDataGridChildRow(int id)
        {
            foreach (DataGridViewRow row in dataGridViewChild.Rows)
            {
                if (row.Cells[_childIDName].Value.ToString().Equals(id.ToString()))
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
                // select columns
                string columns = "*"; // default, all
                if (_childSelectColumns.Length > 0) // custom select
                {
                    columns = _childIDName + ", " + _childParentIDName + ", " + _childSelectColumns;
                }

                string sql = string.Format("SELECT {0} FROM {1} WHERE {2} = @id", columns, _childTable, _childParentIDName);
                SqlCommand command = new SqlCommand(sql, _connection);
                command.Parameters.AddWithValue("@id", parent_id.ToString());

                _childDataAdapter = new SqlDataAdapter(command); // does select by default
                _childDataSet.Clear();
                _childDataAdapter.Fill(_childDataSet, _childTable); // fill data

                // update view
                dataGridViewChild.DataSource = _childDataSet.Tables[_childTable];

                dataGridViewChild.ClearSelection();
                _childID = -1;
                textBoxID.Text = "";
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // add child
        private void buttonAddChild_Click(object sender, EventArgs e)
        {
            Console.WriteLine("Clicked  buttonAddChild");
            if (_parentID == -1)
            {
                MessageError("Parent is negative");
                return;
            }

            try
            {
                // prepare sql string
                List<string> columns = new List<string>(), 
                             values = new List<string>();

                // TODO fix, when setting parent id editable in the config file, maybe?
                columns.Add(_childParentIDName);
                values.Add("@parent_id");
                foreach (KeyValuePair<string, ChildColumn> entry in _childColumns)
                {
                    columns.Add(entry.Key);
                    values.Add("@" + entry.Key);
                }

                string sql = string.Format(
                    "INSERT INTO {0}({1}) VALUES({2})", 
                    _childTable, 
                    string.Join(", ", columns.ToArray()), 
                    string.Join(", ", values.ToArray())
                );
                Console.WriteLine(sql);
                
                // prepare params
                _childDataAdapter.InsertCommand = new SqlCommand(sql, _connection);

                // treat link to parent as separate
                _childDataAdapter.InsertCommand.Parameters.Add("@parent_id", SqlDbType.Int).Value = _parentID;

                // normal params
                foreach (KeyValuePair<string, ChildColumn> entry in _childColumns)
                {
                    _childDataAdapter.InsertCommand.Parameters.Add("@" + entry.Key, entry.Value.DbType).Value = entry.Value.Textbox.Text;
                }

                // create new child
                _childDataAdapter.InsertCommand.ExecuteNonQuery();

                RefillDataGridChild(_parentID);
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message + "\nLine Number" + ex.LineNumber);
            }
        }

        // update child clicked
        private void buttonUpdateChild_Click(object sender, EventArgs e)
        {
            Console.WriteLine("Clicked buttonUpdateChild");
            if (_childID == -1)
            {
                MessageError("Child is negative");
                return;
            }
            int rowIndex = GetDataGridChildRow(_childID);
            if (rowIndex == -1)
            {
                MessageError("Row index is negative");
                return;
            }

            try
            {
                // prepare sql string
                List<string> set = new List<string>(),
                             columns = new List<string>();

                foreach (KeyValuePair<string, ChildColumn> entry in _childColumns)
                {
                    set.Add(entry.Key + " = " + "@" + entry.Key);
                }

                string sql = string.Format(
                    "UPDATE {0} SET {1} WHERE {2} = @id", 
                    _childTable,
                    string.Join(", ", set.ToArray()),
                    _childIDName
                );
                Console.WriteLine(sql);

                // prepare params
                _childDataAdapter.UpdateCommand = new SqlCommand(sql, _connection);
                foreach (KeyValuePair<string, ChildColumn> entry in _childColumns)
                {
                    _childDataAdapter.UpdateCommand.Parameters.Add("@" + entry.Key, entry.Value.DbType).Value = entry.Value.Textbox.Text;
                }
                _childDataAdapter.UpdateCommand.Parameters.Add("@id", SqlDbType.Int).Value = _childID;

                // update child
                _childDataAdapter.UpdateCommand.ExecuteNonQuery();

                RefillDataGridChild(_parentID);
            }
            catch (SqlException ex)
            {
                MessageError(ex.Message);
            }
        }

        // remove child
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
                _childDataSet.Tables[_childTable].Rows[rowIndex].Delete();

                // push
                new SqlCommandBuilder(_childDataAdapter);
                _childDataAdapter.Update(_childDataSet, _childTable);
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
                _connection = new SqlConnection(_connectionString);
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
