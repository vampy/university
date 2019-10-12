using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;
namespace chat.persistence
{
    public abstract class Persistence
    {
        private static Persistence instance = null;

        public abstract IUserRepository createUserRepository();

        public static Persistence getInstance()
        {
            if (instance == null)
            {
                Assembly assem = Assembly.GetExecutingAssembly();
                Type[] types = assem.GetTypes();
                foreach (var type in types)
                {
                    if (type.IsSubclassOf(typeof(Persistence)))
                        instance = (Persistence)Activator.CreateInstance(type);
                }
            }
            return instance;
        }
    }
}
