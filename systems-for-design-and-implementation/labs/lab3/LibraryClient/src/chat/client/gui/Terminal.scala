package chat.client.gui

import java.awt._
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import javax.swing._

import library.model.BookLoan
import library.services.LibraryException

object Terminal
{
    private val DEBUG: Boolean = true

    def showInputDialog(parent: Component, message: String): String =
    {
        showInputDialog(parent, message, "Input")
    }

    def showInputDialog(parent: Component, message: String, title: String): String =
    {
        val options: Array[AnyRef] = Array("OK")
        val panel: JPanel = new JPanel
        val lbl: JLabel = new JLabel(message)
        val txt: JTextField = new JTextField(10)
        panel.add(lbl)
        panel.add(txt)
        JOptionPane
            .showOptionDialog(parent, panel, title, JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                options(0))
        val text: String = txt.getText.trim
        if (text.length > 0)
        {
            return text
        }
        null
    }

    def showErrorDialog(parent: Component, message: String)
    {
        println("ErrorDIALOG")
        JOptionPane.showMessageDialog(parent, message, "ERROR", JOptionPane.ERROR_MESSAGE)
    }

    def showSuccessDialog(parent: Component, message: String)
    {
        JOptionPane.showMessageDialog(parent, message, "SUCCESS", JOptionPane.INFORMATION_MESSAGE)
    }

    def run(controller: TerminalController)
    {
        EventQueue.invokeLater(new Runnable()
        {
            def run()
            {
                try
                {
                    new Terminal(controller)
                }
                catch
                {
                    case e: Exception => e.printStackTrace()
                }
            }
        })
    }
}

class Terminal(var controller: TerminalController)
{
    private var main_panel: JPanel = null
    private var search_textbox: JTextField = null
    private var search_button: JButton = null
    private var table: JTable = null
    private var button_borrow: JButton = null
    private var tableUser: JTable = null
    private var button_return: JButton = null
    private var button_reset: JButton = null
    private var bottom_half: JPanel = null
    private var search_top: JPanel = null
    private var top_half: JPanel = null
    private var bottom_label: JLabel = null

    val frame: JFrame = new JFrame("--- Terminal: " + controller.getUser.getUsername + " ---")

    // valoareeeeeeeee
    $$$setupUI$$$()
    frame.setContentPane(main_panel)
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    frame.pack()
    frame.setVisible(true)
    frame.setSize(800, 600)
    frame.addWindowListener(new WindowAdapter()
    {
        override def windowClosing(e: WindowEvent)
        {
            controller.logout()
        }
    })
    if (controller.isLibrarian)
    {
        button_return.addActionListener(new ActionListener()
        {
            def actionPerformed(e: ActionEvent)
            {
                println("Clicked button return")
                val row: Int = tableUser.getSelectedRow
                if (row >= 0)
                {
                    val book: BookLoan = controller.getTableLibrarianModel.get(row)
                    try
                    {
                        controller.returnBook(row)
                    }
                    catch
                    {
                        case ex: LibraryException =>
                            Terminal.showErrorDialog(main_panel, ex.getMessage)
                    }
                }
            }
        })
        bottom_label.setText("All books")
        search_top.setVisible(false)
        top_half.setVisible(false)
        tableUser.setModel(controller.getTableLibrarianModel)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    }
    else
    {
        table.setModel(controller.getTableAllModel)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.getColumnModel.getColumn(0).setPreferredWidth(2)
        table.getColumnModel.getColumn(1).setPreferredWidth(3)
        tableUser.setModel(controller.getTableUserModel)
        tableUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        search_button.addActionListener(new ActionListener()
        {
            def actionPerformed(e: ActionEvent)
            {
                val title: String = search_textbox.getText
                println("Clicked search button: " + title)
                try
                {
                    controller.search(title)
                }
                catch
                {
                    case ex: LibraryException =>
                        Terminal.showErrorDialog(frame, ex.getMessage)
                }
            }
        })
        button_reset.addActionListener(new ActionListener()
        {
            def actionPerformed(e: ActionEvent)
            {
                search_textbox.setText("")
                println("Clicked reset search button")
                try
                {
                    controller.reset()
                }
                catch
                {
                    case ex: LibraryException =>
                        Terminal.showErrorDialog(frame, ex.getMessage)
                }
            }
        })
        button_borrow.addActionListener(new ActionListener()
        {
            def actionPerformed(e: ActionEvent)
            {
                println("Clicked button loanBook")
                val row: Int = table.getSelectedRow
                if (row >= 0)
                {
                    try
                    {
                        controller.borrowBook(row)
                    }
                    catch
                    {
                        case ex: LibraryException =>
                            Terminal.showErrorDialog(main_panel, ex.getMessage)
                    }
                }
            }
        })
        button_return.setVisible(false)
    }

    private def $$$setupUI$$$()
    {
        main_panel = new JPanel
        main_panel.setLayout(new GridBagLayout)
        search_top = new JPanel
        search_top.setLayout(new GridBagLayout)
        var gbc: GridBagConstraints = null
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weightx = 1.0
        gbc.weighty = 0.01
        gbc.fill = GridBagConstraints.BOTH
        gbc.insets = new Insets(0, 5, 0, 0)
        main_panel.add(search_top, gbc)
        val label1: JLabel = new JLabel
        label1.setText("Book title TEST:")
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weighty = 1.0
        gbc.anchor = GridBagConstraints.WEST
        search_top.add(label1, gbc)
        search_textbox = new JTextField
        gbc = new GridBagConstraints
        gbc.gridx = 1
        gbc.gridy = 0
        gbc.weightx = 1.0
        gbc.weighty = 1.0
        gbc.anchor = GridBagConstraints.WEST
        gbc.fill = GridBagConstraints.HORIZONTAL
        search_top.add(search_textbox, gbc)
        search_button = new JButton
        search_button.setText("Search")
        gbc = new GridBagConstraints
        gbc.gridx = 2
        gbc.gridy = 0
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        search_top.add(search_button, gbc)
        button_reset = new JButton
        button_reset.setText("Reset")
        gbc = new GridBagConstraints
        gbc.gridx = 3
        gbc.gridy = 0
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        search_top.add(button_reset, gbc)
        top_half = new JPanel
        top_half.setLayout(new GridBagLayout)
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 1
        gbc.weightx = 1.0
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        main_panel.add(top_half, gbc)
        val scrollPane1: JScrollPane = new JScrollPane
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 1
        gbc.weightx = 1.0
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        top_half.add(scrollPane1, gbc)
        table = new JTable
        scrollPane1.setViewportView(table)
        button_borrow = new JButton
        button_borrow.setText("Borrow selected book")
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 2
        gbc.weightx = 1.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        top_half.add(button_borrow, gbc)
        val label2: JLabel = new JLabel
        label2.setText("All books")
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weightx = 1.0
        top_half.add(label2, gbc)
        bottom_half = new JPanel
        bottom_half.setLayout(new GridBagLayout)
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 3
        gbc.gridheight = 3
        gbc.weightx = 1.0
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        main_panel.add(bottom_half, gbc)
        val scrollPane2: JScrollPane = new JScrollPane
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 1
        gbc.weightx = 1.0
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        bottom_half.add(scrollPane2, gbc)
        tableUser = new JTable
        scrollPane2.setViewportView(tableUser)
        bottom_label = new JLabel
        bottom_label.setText("Borrowed books")
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weightx = 1.0
        bottom_half.add(bottom_label, gbc)
        button_return = new JButton
        button_return.setText("Return selected book")
        gbc = new GridBagConstraints
        gbc.gridx = 0
        gbc.gridy = 2
        gbc.weightx = 1.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        bottom_half.add(button_return, gbc)
    }
}
