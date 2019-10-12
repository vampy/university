namespace Labs
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.flowLayoutPanel2 = new System.Windows.Forms.FlowLayoutPanel();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.dataGridViewChild = new System.Windows.Forms.DataGridView();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.dataGridViewParent = new System.Windows.Forms.DataGridView();
            this.tableLayoutChild = new System.Windows.Forms.TableLayoutPanel();
            this.buttonAddChild = new System.Windows.Forms.Button();
            this.buttonUpdateChild = new System.Windows.Forms.Button();
            this.buttonRemoveChild = new System.Windows.Forms.Button();
            this.splitter1 = new System.Windows.Forms.Splitter();
            this.buttonDisplayParent = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.textBoxID = new System.Windows.Forms.TextBox();
            this.flowLayoutPanel2.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.groupBox1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewChild)).BeginInit();
            this.groupBox3.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParent)).BeginInit();
            this.SuspendLayout();
            // 
            // flowLayoutPanel2
            // 
            this.flowLayoutPanel2.AutoSize = true;
            this.flowLayoutPanel2.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.flowLayoutPanel2.Controls.Add(this.label1);
            this.flowLayoutPanel2.Controls.Add(this.textBoxID);
            this.flowLayoutPanel2.Controls.Add(this.buttonAddChild);
            this.flowLayoutPanel2.Controls.Add(this.buttonUpdateChild);
            this.flowLayoutPanel2.Controls.Add(this.buttonRemoveChild);
            this.flowLayoutPanel2.Controls.Add(this.splitter1);
            this.flowLayoutPanel2.Controls.Add(this.buttonDisplayParent);
            this.flowLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.flowLayoutPanel2.Location = new System.Drawing.Point(533, 3);
            this.flowLayoutPanel2.Name = "flowLayoutPanel2";
            this.flowLayoutPanel2.Size = new System.Drawing.Size(541, 190);
            this.flowLayoutPanel2.TabIndex = 1;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 49.23339F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50.76661F));
            this.tableLayoutPanel1.Controls.Add(this.flowLayoutPanel2, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.groupBox1, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.groupBox3, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.tableLayoutChild, 1, 1);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 2;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 35.05338F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 64.94662F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 20F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(1077, 562);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.dataGridViewChild);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox1.Location = new System.Drawing.Point(3, 199);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(524, 360);
            this.groupBox1.TabIndex = 2;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Children";
            // 
            // dataGridViewChild
            // 
            this.dataGridViewChild.AllowUserToAddRows = false;
            this.dataGridViewChild.AllowUserToDeleteRows = false;
            this.dataGridViewChild.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridViewChild.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewChild.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dataGridViewChild.Location = new System.Drawing.Point(3, 16);
            this.dataGridViewChild.MultiSelect = false;
            this.dataGridViewChild.Name = "dataGridViewChild";
            this.dataGridViewChild.ReadOnly = true;
            this.dataGridViewChild.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dataGridViewChild.Size = new System.Drawing.Size(518, 341);
            this.dataGridViewChild.TabIndex = 1;
            this.dataGridViewChild.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridViewChild_CellClick);
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.dataGridViewParent);
            this.groupBox3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox3.Location = new System.Drawing.Point(3, 3);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(524, 190);
            this.groupBox3.TabIndex = 4;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "Parents";
            // 
            // dataGridViewParent
            // 
            this.dataGridViewParent.AllowUserToAddRows = false;
            this.dataGridViewParent.AllowUserToDeleteRows = false;
            this.dataGridViewParent.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridViewParent.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewParent.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dataGridViewParent.Location = new System.Drawing.Point(3, 16);
            this.dataGridViewParent.MultiSelect = false;
            this.dataGridViewParent.Name = "dataGridViewParent";
            this.dataGridViewParent.ReadOnly = true;
            this.dataGridViewParent.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dataGridViewParent.Size = new System.Drawing.Size(518, 171);
            this.dataGridViewParent.TabIndex = 1;
            this.dataGridViewParent.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridViewParent_CellClick);
            // 
            // tableLayoutChild
            // 
            this.tableLayoutChild.AutoScroll = true;
            this.tableLayoutChild.CellBorderStyle = System.Windows.Forms.TableLayoutPanelCellBorderStyle.Single;
            this.tableLayoutChild.ColumnCount = 2;
            this.tableLayoutChild.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutChild.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutChild.Dock = System.Windows.Forms.DockStyle.Top;
            this.tableLayoutChild.Location = new System.Drawing.Point(533, 199);
            this.tableLayoutChild.Name = "tableLayoutChild";
            this.tableLayoutChild.RowCount = 1;
            this.tableLayoutChild.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutChild.Size = new System.Drawing.Size(541, 100);
            this.tableLayoutChild.TabIndex = 5;
            // 
            // buttonAddChild
            // 
            this.buttonAddChild.Enabled = false;
            this.buttonAddChild.Location = new System.Drawing.Point(73, 3);
            this.buttonAddChild.Name = "buttonAddChild";
            this.buttonAddChild.Size = new System.Drawing.Size(93, 31);
            this.buttonAddChild.TabIndex = 9;
            this.buttonAddChild.Text = "Add child";
            this.buttonAddChild.UseVisualStyleBackColor = true;
            this.buttonAddChild.Click += new System.EventHandler(this.buttonAddChild_Click);
            // 
            // buttonUpdateChild
            // 
            this.buttonUpdateChild.Enabled = false;
            this.buttonUpdateChild.Location = new System.Drawing.Point(172, 3);
            this.buttonUpdateChild.Name = "buttonUpdateChild";
            this.buttonUpdateChild.Size = new System.Drawing.Size(93, 31);
            this.buttonUpdateChild.TabIndex = 7;
            this.buttonUpdateChild.Text = "Update child";
            this.buttonUpdateChild.UseVisualStyleBackColor = true;
            this.buttonUpdateChild.Click += new System.EventHandler(this.buttonUpdateChild_Click);
            // 
            // buttonRemoveChild
            // 
            this.buttonRemoveChild.Enabled = false;
            this.buttonRemoveChild.Location = new System.Drawing.Point(271, 3);
            this.buttonRemoveChild.Name = "buttonRemoveChild";
            this.buttonRemoveChild.Size = new System.Drawing.Size(129, 31);
            this.buttonRemoveChild.TabIndex = 4;
            this.buttonRemoveChild.Text = "Remove child";
            this.buttonRemoveChild.UseVisualStyleBackColor = true;
            this.buttonRemoveChild.Click += new System.EventHandler(this.buttonRemoveChild_Click);
            // 
            // splitter1
            // 
            this.splitter1.Location = new System.Drawing.Point(406, 3);
            this.splitter1.Name = "splitter1";
            this.splitter1.Size = new System.Drawing.Size(10, 31);
            this.splitter1.TabIndex = 10;
            this.splitter1.TabStop = false;
            // 
            // buttonDisplayParent
            // 
            this.buttonDisplayParent.Location = new System.Drawing.Point(422, 3);
            this.buttonDisplayParent.Name = "buttonDisplayParent";
            this.buttonDisplayParent.Size = new System.Drawing.Size(89, 31);
            this.buttonDisplayParent.TabIndex = 5;
            this.buttonDisplayParent.Text = "Display Parents";
            this.buttonDisplayParent.UseVisualStyleBackColor = true;
            this.buttonDisplayParent.Click += new System.EventHandler(this.buttonDisplayParent_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(18, 13);
            this.label1.TabIndex = 6;
            this.label1.Text = "ID";
            // 
            // textBoxID
            // 
            this.textBoxID.Location = new System.Drawing.Point(27, 3);
            this.textBoxID.Name = "textBoxID";
            this.textBoxID.ReadOnly = true;
            this.textBoxID.Size = new System.Drawing.Size(40, 20);
            this.textBoxID.TabIndex = 8;
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1077, 562);
            this.Controls.Add(this.tableLayoutPanel1);
            this.Name = "MainForm";
            this.Text = "Labs MainForm";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
            this.Load += new System.EventHandler(this.MainForm_Load);
            this.flowLayoutPanel2.ResumeLayout(false);
            this.flowLayoutPanel2.PerformLayout();
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewChild)).EndInit();
            this.groupBox3.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParent)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.FlowLayoutPanel flowLayoutPanel2;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.DataGridView dataGridViewParent;
        private System.Windows.Forms.DataGridView dataGridViewChild;
        private System.Windows.Forms.TableLayoutPanel tableLayoutChild;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox textBoxID;
        private System.Windows.Forms.Button buttonAddChild;
        private System.Windows.Forms.Button buttonUpdateChild;
        private System.Windows.Forms.Button buttonRemoveChild;
        private System.Windows.Forms.Splitter splitter1;
        private System.Windows.Forms.Button buttonDisplayParent;


    }
}

