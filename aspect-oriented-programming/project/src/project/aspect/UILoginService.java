package project.aspect;

import ajia.security.LoginService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.swing.*;

public class UILoginService implements LoginService
{
    @Override
    public Authentication getAuthenticationToken()
    {
        JLabel jUserName = new JLabel("User Name");
        JTextField userName = new JTextField();
        JLabel jPassword = new JLabel("Password");
        JTextField password = new JPasswordField();
        Object[] ob = {jUserName, userName, jPassword, password};
        int result = JOptionPane.showConfirmDialog(null, ob,
            "Please input password for JOptionPane showConfirmDialog", JOptionPane.DEFAULT_OPTION);

        return new UsernamePasswordAuthenticationToken(userName.getText(), password.getText());
    }
}
