
// This file has been generated by the GUI designer. Do not modify.
namespace View
{
	public partial class GUIExpressionDialog
	{
		private global::Gtk.HBox hbox2;
		
		private global::Gtk.Button button_const;
		
		private global::Gtk.Button button_var;
		
		private global::Gtk.HBox hbox3;
		
		private global::Gtk.Button button_arhitmetic1;
		
		private global::Gtk.Button button_heap_new;
		
		private global::Gtk.Button button_heap_read;
		
		private global::Gtk.HBox hbox1;
		
		private global::Gtk.Label label_value;
		
		private global::Gtk.Button buttonOk;

		protected virtual void Build ()
		{
			global::Stetic.Gui.Initialize (this);
			// Widget View.GUIExpressionDialog
			this.Name = "View.GUIExpressionDialog";
			this.WindowPosition = ((global::Gtk.WindowPosition)(3));
			this.Modal = true;
			// Internal child View.GUIExpressionDialog.VBox
			global::Gtk.VBox w1 = this.VBox;
			w1.Name = "dialog1_VBox";
			w1.BorderWidth = ((uint)(2));
			// Container child dialog1_VBox.Gtk.Box+BoxChild
			this.hbox2 = new global::Gtk.HBox ();
			this.hbox2.Name = "hbox2";
			this.hbox2.Spacing = 6;
			// Container child hbox2.Gtk.Box+BoxChild
			this.button_const = new global::Gtk.Button ();
			this.button_const.CanFocus = true;
			this.button_const.Name = "button_const";
			this.button_const.UseUnderline = true;
			this.button_const.Label = global::Mono.Unix.Catalog.GetString ("Constant");
			this.hbox2.Add (this.button_const);
			global::Gtk.Box.BoxChild w2 = ((global::Gtk.Box.BoxChild)(this.hbox2 [this.button_const]));
			w2.Position = 0;
			w2.Expand = false;
			w2.Fill = false;
			// Container child hbox2.Gtk.Box+BoxChild
			this.button_var = new global::Gtk.Button ();
			this.button_var.CanFocus = true;
			this.button_var.Name = "button_var";
			this.button_var.UseUnderline = true;
			this.button_var.Label = global::Mono.Unix.Catalog.GetString ("Variable");
			this.hbox2.Add (this.button_var);
			global::Gtk.Box.BoxChild w3 = ((global::Gtk.Box.BoxChild)(this.hbox2 [this.button_var]));
			w3.Position = 1;
			w3.Expand = false;
			w3.Fill = false;
			// Container child hbox2.Gtk.Box+BoxChild
			this.hbox3 = new global::Gtk.HBox ();
			this.hbox3.Name = "hbox3";
			this.hbox3.Spacing = 6;
			// Container child hbox3.Gtk.Box+BoxChild
			this.button_arhitmetic1 = new global::Gtk.Button ();
			this.button_arhitmetic1.CanFocus = true;
			this.button_arhitmetic1.Name = "button_arhitmetic1";
			this.button_arhitmetic1.UseUnderline = true;
			this.button_arhitmetic1.Label = global::Mono.Unix.Catalog.GetString ("Arithmetic");
			this.hbox3.Add (this.button_arhitmetic1);
			global::Gtk.Box.BoxChild w4 = ((global::Gtk.Box.BoxChild)(this.hbox3 [this.button_arhitmetic1]));
			w4.Position = 0;
			w4.Expand = false;
			w4.Fill = false;
			// Container child hbox3.Gtk.Box+BoxChild
			this.button_heap_new = new global::Gtk.Button ();
			this.button_heap_new.CanFocus = true;
			this.button_heap_new.Name = "button_heap_new";
			this.button_heap_new.UseUnderline = true;
			this.button_heap_new.Label = global::Mono.Unix.Catalog.GetString ("Heap new");
			this.hbox3.Add (this.button_heap_new);
			global::Gtk.Box.BoxChild w5 = ((global::Gtk.Box.BoxChild)(this.hbox3 [this.button_heap_new]));
			w5.Position = 1;
			w5.Expand = false;
			w5.Fill = false;
			// Container child hbox3.Gtk.Box+BoxChild
			this.button_heap_read = new global::Gtk.Button ();
			this.button_heap_read.CanFocus = true;
			this.button_heap_read.Name = "button_heap_read";
			this.button_heap_read.UseUnderline = true;
			this.button_heap_read.Label = global::Mono.Unix.Catalog.GetString ("Heap read");
			this.hbox3.Add (this.button_heap_read);
			global::Gtk.Box.BoxChild w6 = ((global::Gtk.Box.BoxChild)(this.hbox3 [this.button_heap_read]));
			w6.Position = 2;
			w6.Expand = false;
			w6.Fill = false;
			this.hbox2.Add (this.hbox3);
			global::Gtk.Box.BoxChild w7 = ((global::Gtk.Box.BoxChild)(this.hbox2 [this.hbox3]));
			w7.Position = 2;
			w7.Expand = false;
			w7.Fill = false;
			w1.Add (this.hbox2);
			global::Gtk.Box.BoxChild w8 = ((global::Gtk.Box.BoxChild)(w1 [this.hbox2]));
			w8.Position = 0;
			w8.Expand = false;
			w8.Fill = false;
			// Internal child View.GUIExpressionDialog.ActionArea
			global::Gtk.HButtonBox w9 = this.ActionArea;
			w9.Name = "dialog1_ActionArea";
			w9.Spacing = 10;
			w9.BorderWidth = ((uint)(5));
			w9.LayoutStyle = ((global::Gtk.ButtonBoxStyle)(4));
			// Container child dialog1_ActionArea.Gtk.ButtonBox+ButtonBoxChild
			this.hbox1 = new global::Gtk.HBox ();
			this.hbox1.Name = "hbox1";
			this.hbox1.Homogeneous = true;
			// Container child hbox1.Gtk.Box+BoxChild
			this.label_value = new global::Gtk.Label ();
			this.label_value.Name = "label_value";
			this.label_value.LabelProp = global::Mono.Unix.Catalog.GetString ("<value>");
			this.hbox1.Add (this.label_value);
			global::Gtk.Box.BoxChild w10 = ((global::Gtk.Box.BoxChild)(this.hbox1 [this.label_value]));
			w10.Position = 0;
			w10.Expand = false;
			w10.Fill = false;
			// Container child hbox1.Gtk.Box+BoxChild
			this.buttonOk = new global::Gtk.Button ();
			this.buttonOk.CanDefault = true;
			this.buttonOk.CanFocus = true;
			this.buttonOk.Name = "buttonOk";
			this.buttonOk.UseStock = true;
			this.buttonOk.UseUnderline = true;
			this.buttonOk.Label = "gtk-ok";
			this.hbox1.Add (this.buttonOk);
			global::Gtk.Box.BoxChild w11 = ((global::Gtk.Box.BoxChild)(this.hbox1 [this.buttonOk]));
			w11.Position = 1;
			w11.Expand = false;
			w11.Fill = false;
			w9.Add (this.hbox1);
			global::Gtk.ButtonBox.ButtonBoxChild w12 = ((global::Gtk.ButtonBox.ButtonBoxChild)(w9 [this.hbox1]));
			w12.Expand = false;
			w12.Fill = false;
			w12.Padding = ((uint)(5));
			if ((this.Child != null)) {
				this.Child.ShowAll ();
			}
			this.DefaultWidth = 434;
			this.DefaultHeight = 67;
			this.Show ();
			this.Close += new global::System.EventHandler (this.OnClose);
			this.DeleteEvent += new global::Gtk.DeleteEventHandler (this.OnDeleteEvent);
			this.button_const.Clicked += new global::System.EventHandler (this.OnButtonConstClicked);
			this.button_var.Clicked += new global::System.EventHandler (this.OnButtonVarClicked);
			this.button_arhitmetic1.Clicked += new global::System.EventHandler (this.OnButtonArhitmetic1Clicked);
			this.button_heap_new.Clicked += new global::System.EventHandler (this.OnButtonHeapNewClicked);
			this.button_heap_read.Clicked += new global::System.EventHandler (this.OnButtonHeapReadClicked);
			this.buttonOk.Clicked += new global::System.EventHandler (this.OnButtonOkClicked);
		}
	}
}