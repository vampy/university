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

//Listing 15.8 Mapping-based security attribute definition source 
package ajia.security;

import org.aspectj.lang.JoinPoint;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MappingBasedSecurityMetadataSource implements SecurityMetadataSource
{

    private Map<String, String> mapping;

    public List<ConfigAttribute> getAttributes(Object jp)
    {
        String methodName = ((JoinPoint) jp).getSignature().getName();
        for (String pattern : mapping.keySet())
        {
            if (PatternMatchUtils.simpleMatch(pattern, methodName))
            {
                String role = mapping.get(pattern);
                return SecurityConfig.createList(role);
            }
        }
        throw new IllegalArgumentException("Unknown mapping for " + methodName);
    }

    public Collection<ConfigAttribute> getAllConfigAttributes()
    {
        return null;
    }

    public boolean supports(Class clazz)
    {
        return clazz.isAssignableFrom(JoinPoint.class);
    }

    public void setRoleMapping(Map<String, String> mapping)
    {
        this.mapping = mapping;
    }
}