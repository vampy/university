using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace chat.persistence
{
    using repository.file;
    class FilePersistence:Persistence
    {
        public override IUserRepository createUserRepository()
        {
            Console.WriteLine("File ");
            return new UserRepositoryTextFile();
        }
    }
}
