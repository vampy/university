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

//Listing 15.1 Base authentication aspect
package ajia.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class AbstractAuthenticationAspect
{
    private AuthenticationSupport authenticationSupport;

    public AbstractAuthenticationAspect()
    {
        System.out.println("AbstractAuthenticationAspect: Constructor");
    }

    @Pointcut
    public abstract void authenticationRequired();

    @Before("authenticationRequired()")
    public void authenticate()
    {
        authenticationSupport.authenticate();
    }

    public void setAuthenticationSupport(AuthenticationSupport authenticationSupport)
    {
        this.authenticationSupport = authenticationSupport;
    }
}