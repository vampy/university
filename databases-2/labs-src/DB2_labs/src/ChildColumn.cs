using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Labs
{
    class ChildColumn
    {
        private TextBox textbox;

        public TextBox Textbox
        {
            get { return textbox; }
            set { textbox = value; }
        }
        private string name;

        public string Name
        {
            get { return name; }
            set { name = value; }
        }
        private SqlDbType dbType;

        public SqlDbType DbType
        {
            get { return dbType; }
            set { dbType = value; }
        }
    }
}
