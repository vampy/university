using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.Serialization;
using System.Collections.Generic;
using System.Runtime.Serialization.Formatters.Binary;
using Exceptions;
using Model;

namespace Repository
{
    /// <summary>
    /// The type Mem repository.
    /// </summary>
    public class MemRepository : IRepository
    {
        private ProgramState currentProgram;
        private List<ProgramState> programs = new List<ProgramState>();
        private int currentProgramIndex = 0;
        private int availableID = 0;

        private const string filename_serialized = "program.toy.ser";
        private const string filename = "program.toy";

        private void SetProgramID(ProgramState program)
        {
            if (program.GetID() == -1)
            {
                program.SetID(availableID);
                availableID++; // next available
            }
        }

        /// <summary>
        /// Instantiates a new Mem repository.
        /// </summary>
        /// <param name="programState"> the program state </param>
        public MemRepository(ProgramState mainProgram)
        {
            this.currentProgram = mainProgram;
            SetProgramID(currentProgram);
        }

        public void AddProgram(ProgramState program)
        {
            programs.Add(program);
            SetProgramID(program);
        }

        public void RemoveProgram(ProgramState program)
        {
            programs.Remove(program);
        }

        public bool NextCurrentProgram()
        {
            if (!HasPrograms())
            {
                return false;
            }

            // can execute a next program
            if (currentProgramIndex + 1 < programs.Count)
            {
                // move forward
                currentProgramIndex++;
                currentProgram = programs[currentProgramIndex];
                return true;
            }

            // reset
            currentProgramIndex = 0;
            currentProgram = programs[currentProgramIndex];
            return false;
        }

        /// <inheritdoc />
        public void SetMain(Statement statement)
        {
            currentProgram.SetProgram(statement);
        }

        /// <inheritdoc />
        public bool StartMain()
        {
            Debug.Assert(programs.Count == 0);
            Debug.Assert(currentProgramIndex == 0);
            AddProgram(currentProgram);
            currentProgramIndex = 0;

            return currentProgram.Start();
        }

        public void StopMain()
        {
            programs.Clear();
            currentProgramIndex = 0;

            currentProgram.Stop(true);
        }

        public ProgramState GetCP()
        {
            return currentProgram;
        }

        /// <inheritdoc />
        public void StopCP()
        {
            RemoveProgram(currentProgram);

            currentProgramIndex--; // reduce index, we remove a program
            if (currentProgramIndex < 0)
            {
                currentProgramIndex = 0;
            }

            currentProgram.Stop(false);
        }

        /// <inheritdoc />
        public bool HasPrograms()
        {
            return programs.Count != 0;
        }

        public bool SerializeToFile()
        {
            if (currentProgram == null)
            {
                return false;
            }

            try
            {
                FileStream fs = File.Create(filename_serialized);

                BinaryFormatter bformatter = new BinaryFormatter();

                bformatter.Serialize(fs, currentProgram);

                fs.Close();
                return true;
            }
            catch (SerializationException e)
            {
                Console.WriteLine("ERROR: could not write: " + e.Message);
            }
            catch (IOException e)
            {
                Console.WriteLine("ERROR: could not write: " + e.Message);
            }
          
            return false;
        }

        public bool SerializeFromFile()
        {
            try
            {
                FileStream fs = File.OpenRead(filename_serialized);
                BinaryFormatter bformatter = new BinaryFormatter();

                currentProgram = (ProgramState) bformatter.Deserialize(fs);

                fs.Close();
                return true;
            }
            catch (SerializationException e)
            {
                Console.WriteLine("ERROR: could not read: " + e.Message);
            }
            catch (IOException e)
            {
                Console.WriteLine("ERROR: could not read: " + e.Message);
            }

            return false;
        }

        public bool SaveToFile()
        {
            if (currentProgram == null)
            {
                return false;
            }

            try
            {
                StreamWriter writer = new StreamWriter(filename);

                //writer.WriteLine(this.ToString());

                writer.WriteLine("// ExeStack content:");
                writer.WriteLine(currentProgram.Stack.AllSave);

                writer.WriteLine("// SymTable content:");
                writer.WriteLine(currentProgram.Table.AllSave);

                writer.WriteLine("// Out content:");
                writer.Write(currentProgram.Out.All);

                writer.Close();
                return true;
            }
            catch (IOException e)
            {
                Console.WriteLine("ERROR: could not save: " + e.Message);
            }

            return false;
        }

        /// <inheritdoc />
        public override string ToString()
        {
            string ret = "";

            if (programs.Count > 1)
            {
                for (int i = 1; i < programs.Count; i++)
                {
                    ret += string.Format("{0}\n", programs[i].ToString());
                }
            }
            ret += string.Format("currentProgram = {0}", currentProgram.ToString());

            return ret;
        }
    }

}

