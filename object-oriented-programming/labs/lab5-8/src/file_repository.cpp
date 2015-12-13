#include <fstream>
#include "file_repository.hpp"
#include "util.hpp"

void FileRepository::writeToFile() const
{
    /*
     * File will be in the following format
     * first line will have the number of ingredients
     * followed by a new line
     *
     *
    */

    ofstream fileHandle;

    fileHandle.open(this->filename, ios::out | ios::trunc);

    if(fileHandle.is_open())
    {
        // write length
        fileHandle << this->list->getLength() << "\n" << "\n";

        auto iterator = this->list->getIterator();
        while(iterator->hasNext())
        {
            Ingredient *temp_ingredient = iterator->next();

            // write ingredient
            fileHandle << temp_ingredient->getId() << "\n"
                       << temp_ingredient->getQuantity() << "\n"
                       << temp_ingredient->getName() << "\n"
                       << temp_ingredient->getProducer() << "\n"
                       << "\n"; // separate by newline
        }
    }
    else
    {
        printDebug("ERROR writeToFile: unable to open file");
    }
    fileHandle.close();
}

void FileRepository::readFromFile()
{
    ifstream fileHandle;

    fileHandle.open(this->filename, ios::in);

    // assume file is correct, TODO
    if(fileHandle.is_open())
    {
        // read the length
        string temp_line;
        int length;

        getline(fileHandle, temp_line);
        if(!stringToInt(temp_line, length))
        {
            throw RepositoryException("Length in the header is not an integer");
        }
        if(length < 0)
        {
            throw RepositoryException("Length is negative");
        }


        printDebug("Read length: " + temp_line);

        // read all ingredients
        for(int i = 0; i < length; i++)
        {
            unsigned int id;
            unsigned int quantity;
            string name;
            string producer;

            //cout << "i = " << i << endl;
            // ignore next line after input, foreach ingredient
            getline(fileHandle, temp_line);


            // id
            getline(fileHandle, temp_line);
            if(!stringToInt(temp_line, id))
            {
                throw RepositoryException("id is not a number");
            }

            // quantity
            getline(fileHandle, temp_line);
            if(!stringToInt(temp_line, quantity))
            {
                throw RepositoryException("quantity is not a number");
            }

            // name
            getline(fileHandle, name);
//            cout << temp_line << endl;

            // producer
            getline(fileHandle, producer);
//            cout << temp_line << endl;

            this->addIngredient(new Ingredient(id, quantity, name, producer));
        }
    }
    else
    {
        printDebug("ERROR readFromFile: unable to open file");
        throw RepositoryException("Unable to open file");
    }
    fileHandle.close();
}

void FileRepository::removeIngredient(unsigned int id)
{
    MemRepository::removeIngredient(id);
    this->writeToFile();
}

void FileRepository::addIngredient(Ingredient *ingredient)
{
    MemRepository::addIngredient(ingredient);
    this->writeToFile();
}

void FileRepository::filterBy(const RepositoryFilter &filter)
{
    MemRepository::filterBy(filter);
    this->writeToFile();
}

bool FileRepository::undo()
{
    if(MemRepository::undo())
    {
        this->writeToFile();
        return true;
    }

    return false;
}

void FileRepository::sortBy(repositorySortFunc sortfunc)
{
    MemRepository::sortBy(sortfunc);
    this->writeToFile();
}
