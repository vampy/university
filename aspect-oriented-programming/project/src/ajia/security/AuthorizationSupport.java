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

//Listing 15.6 Support class simplifying implementation and testing
package ajia.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class AuthorizationSupport
{
    private AccessDecisionManager  accessDecisionManager;
    private SecurityMetadataSource securityMetadataSource;

    public void authorize(Object securedObject)
    {
        Collection<ConfigAttribute> attrs = securityMetadataSource
            .getAttributes(securedObject);
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        accessDecisionManager.decide(authentication, securedObject, attrs);
    }

    public void setAccessDecisionManager(AccessDecisionManager adm)
    {
        this.accessDecisionManager = adm;
    }

    public void setSecurityMetadataSource(SecurityMetadataSource smds)
    {
        this.securityMetadataSource = smds;
    }
}