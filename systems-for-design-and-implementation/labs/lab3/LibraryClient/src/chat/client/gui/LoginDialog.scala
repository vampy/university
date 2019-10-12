package chat.client.gui

import java.awt._
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import javax.swing.border.LineBorder

import library.services.LibraryException


class LoginDialog(var ctrl: TerminalController) extends JDialog
{
    private var tfUsername: JTextField = null
    private var pfPassword: JPasswordField = null
    private var lbUsername: JLabel = null
    private var lbPassword: JLabel = null
    private var btnLogin: JButton = null
    private var btnCancel: JButton = null

    val panel: JPanel = new JPanel(new GridBagLayout)
    val cs: GridBagConstraints = new GridBagConstraints
    val bp: JPanel = new JPanel
    val self = this

    cs.fill = GridBagConstraints.HORIZONTAL
    lbUsername = new JLabel("Username: ")
    cs.gridx = 0
    cs.gridy = 0
    cs.gridwidth = 1
    panel.add(lbUsername, cs)
    tfUsername = new JTextField(20)
    cs.gridx = 1
    cs.gridy = 0
    cs.gridwidth = 2
    panel.add(tfUsername, cs)
    lbPassword = new JLabel("Password: ")
    cs.gridx = 0
    cs.gridy = 1
    cs.gridwidth = 1
    panel.add(lbPassword, cs)
    pfPassword = new JPasswordField(20)
    cs.gridx = 1
    cs.gridy = 1
    cs.gridwidth = 2
    panel.add(pfPassword, cs)
    panel.setBorder(new LineBorder(Color.GRAY))
    btnLogin = new JButton("Login")

    // https://stackoverflow.com/questions/3239106/scala-actionlistener-anonymous-function-type-mismatch
    implicit def toActionListener(f: ActionEvent => Unit) = new ActionListener
    {
        def actionPerformed(e: ActionEvent)
        {f(e)}
    }

    btnLogin.addActionListener(new ActionListener()
    {
        override def actionPerformed(e: ActionEvent)
        {
            println("Login button pressed.")
            try
            {
                assert(tfUsername != null)
                ctrl.login(getUsername, getPassword)
                Terminal.run(ctrl)
                dispose()
            }
            catch
            {
                case ex: LibraryException =>
                    Terminal.showErrorDialog(self, "Invalid username or password. Details: " + ex.getMessage)
                    ex.printStackTrace()
                    pfPassword.setText("")

            }
        }
    })

    btnCancel = new JButton("Cancel")
    btnCancel.addActionListener
    { e: ActionEvent => dispose() }
    bp.add(btnLogin)
    bp.add(btnCancel)
    getContentPane.add(panel, BorderLayout.CENTER)
    getContentPane.add(bp, BorderLayout.PAGE_END)
    pack()
    setVisible(true)
    setLocationRelativeTo(null)

    def getUsername: String =
    {
        tfUsername.getText.trim
    }

    def getPassword: String =
    {
        new String(pfPassword.getPassword)
    }
}
