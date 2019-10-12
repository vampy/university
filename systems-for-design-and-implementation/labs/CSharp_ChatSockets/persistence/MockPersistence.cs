using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace chat.persistence
{
    using repository.mock;
    class MockPersistence/*: Persistence*/
    {
        public /*override*/ IUserRepository createUserRepository()
        {
            Console.WriteLine("Mock Repository");
            return new UserRepositoryMock();
        }

    }
}
