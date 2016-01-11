#include <iostream>
#include <fstream>
#include <map>
#include <vector>
#include <string>
#include <cstring>
#include <algorithm>
#include <set>
#include <locale>

#include "solve.hpp"

#include "tests/test_vector.hpp"
#include "tests/test_map_vector.hpp"
#include "tests/test_map_hash.hpp"

using namespace std;

void printMenu()
{
    cout << "1. Choose filename to read from('english-words.95' by default)" << endl;
    cout << "2. Choose the anagram of a word" << endl;
    cout << "3. Print all anagrams" << endl;

    cout << "h. Print this help menu" << endl;
    cout << "q. Quit" << endl
         << endl;
}

int main()
{
    // run tests
    testVector();
    testMapVector();
    testMapHash();

    printMenu();
    string filename = "english-words.95", word;
    ifstream fHandle;
    map<string, set<string>> map_std;
    MapHash::Map<set<string>> map_hash;
    MapVector::Map<string, set<string>> map_vector;
    readFromFileADT<MapHash::Map<set<string>>>(fHandle, map_hash, filename);
    // readFromFileADT<MapVector::Map<string, set<string>>>(fHandle, map_vector, filename);
    while (1)
    {
        string option;
        cout << ">> ";
        cin >> option;

        option = trim(option);
        if (option == "1")
        {
            cout << "Filename with words(no spaces) = ";
            cin >> filename;
            map_hash.clear();
            readFromFileADT<MapHash::Map<set<string>>>(fHandle, map_hash, filename);
        }
        else if (option == "2")
        {
            cout << "Word = ";
            cin >> word;
            string key = word;
            sort(key.begin(), key.end());
            if (map_hash.containsKey(key)) // get all anagrams
            {
                cout << "Anagrams: ";
                set<string> anagrams = map_hash.get(key);
                for (auto it = anagrams.begin(); it != anagrams.end(); ++it)
                {
                    cout << *it << " ";
                }
                cout << endl;
            }
            else
            {
                printError("There are no anagrams for the word '" + word + "'");
            }
        }
        else if (option == "3")
        {
            printAllMapADT<MapHash::Map<set<string>>>(map_hash);
            cout << endl
                 << map_hash.getLength() << endl;
        }
        else if (option == "h")
        {
            printMenu();
        }
        else if (option == "q")
        {
            cout << "Quiting" << endl;
            break;
        }
    }

    return 0;
}
