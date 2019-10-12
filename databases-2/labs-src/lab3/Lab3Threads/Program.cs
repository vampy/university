using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Lab3Threads
{
    class Program
    {
        static void Main(string[] args)
        {
            var connectionString = "Data Source=(local);Initial Catalog=leyyin;Integrated Security=SSPI;";

            new Thread (delegate()
            {
                using (var conn = new SqlConnection(connectionString))
                using (var command = new SqlCommand("deadlock_1", conn) { CommandType = CommandType.StoredProcedure })
                {
                    conn.Open();
                    command.ExecuteNonQuery();
                    conn.Close();
                }
                Console.WriteLine("Thread 1");
            }).Start();

            new Thread(delegate()
            {
                using (var conn = new SqlConnection(connectionString))
                using (var command = new SqlCommand("deadlock_2", conn) { CommandType = CommandType.StoredProcedure })
                {
                    conn.Open();
                    command.ExecuteNonQuery();
                    conn.Close();
                }
                Console.WriteLine("Thread 2");
            }).Start();

            Console.ReadLine();
        }
    }
}
