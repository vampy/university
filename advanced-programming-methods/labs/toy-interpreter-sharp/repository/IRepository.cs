using System;
using Exceptions;
using Model;

namespace Repository
{
    /// <summary>
    /// The interface I repository.
    /// </summary>
    public interface IRepository
    {
        void AddProgram(ProgramState program);

        void RemoveProgram(ProgramState program);

        bool NextCurrentProgram();

        // the big program
        void SetMain(Statement statement);
        bool StartMain();
        void StopMain();

        // tiny programs
        ProgramState GetCP();
        void StopCP();

        bool HasPrograms();

        bool SerializeToFile();

        bool SerializeFromFile();

        bool SaveToFile();

        /// <summary>
        /// {@inheritDoc} </summary>
        string ToString();
    }

}

