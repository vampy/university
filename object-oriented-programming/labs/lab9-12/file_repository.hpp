#ifndef FILE_REPOSITORY_H_
#define FILE_REPOSITORY_H_
#include "mem_repository.hpp"

class FileRepositoryException : public MemRepository
{
};

class FileRepository : public MemRepository
{
public:
    FileRepository(string fileToWrite = "ingredients.dat") : MemRepository(), filename(fileToWrite)
    {
        this->readFromFile();
    }

    ~FileRepository() { this->writeToFile(); }

    void writeToFile() const;
    void readFromFile(bool tryWrite = false);

    void setFileName(string newFileName) { this->filename = newFileName; }

    void removeIngredient(unsigned int);

    void addIngredient(Ingredient*);

    void filterBy(const RepositoryFilter&);

    void sortBy(repositorySortFunc);

    bool undo();

protected:
    string filename;
};

#endif // FILE_REPOSITORY_H_
