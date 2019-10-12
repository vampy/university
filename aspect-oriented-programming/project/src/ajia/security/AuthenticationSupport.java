/*
Copyright 2009 Ramnivas Laddad

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 
    http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/

//Listing 15.2 Support class for authentication
package ajia.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationSupport
{
    private AuthenticationManager authenticationManager;
    private LoginService          loginUI;

    public void authenticate()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
        {
            return;
        }
        Authentication authenticationToken = loginUI.getAuthenticationToken();
        authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setAuthenticationManager(AuthenticationManager am)
    {
        this.authenticationManager = am;
    }

    public void setLoginUI(LoginService loginUI)
    {
        this.loginUI = loginUI;
    }
}