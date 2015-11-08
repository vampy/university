#ifndef SOLVE_H_
#define SOLVE_H_
#include <map>
#include <set>
#include <string>

#include "vector.hpp"
#include "map_vector.hpp"
#include "map_hash.hpp"

using namespace std;

void addToMapStd(map<string, set<string>> &anagram, string word)
{
    string key = word;
    sort(key.begin(), key.end());

    if(anagram.find(key) == anagram.end()) // not found, add new key
    {
        set<string> temp_v = {word};
        anagram[key] = temp_v;
    }
    else // found, add to set
    {
        anagram[key].insert(word);
    }
}

template <typename MapType>
void addToMapADT(MapType &map_type, string word)
{
    string key = word;
    sort(key.begin(), key.end());
    //cout << "word = " << word << " --- " << "key = " << key << endl;

    if(!map_type.containsKey(key)) // not found, add new key
    {
        set<string> temp_v = {word};
        map_type.put(key, temp_v);
    }
    else // found, add to set
    {
        auto temp_set = map_type.get(key);
        temp_set.insert(word);
        map_type.put(key, temp_set);
    }
}

void readFromFileStd(ifstream &fHandle, map<string, set<string>> &map_std, string filename)
{
    fHandle.open(filename, ios::in);
    if(!fHandle.is_open())
    {
        printError("Filename '" + filename + "' does not exist");
        return;
    }

    while(!fHandle.eof())
    {
        string word;
        fHandle >> word;

        if(word.length() > 3)
        {
            word = stringToLower(word);
            addToMapStd(map_std, word);
        }
    }
    fHandle.close();
}

template <typename MapType>
void readFromFileADT(ifstream &fHandle, MapType &map_type, string filename)
{
    fHandle.open(filename, ios::in);
    if(!fHandle.is_open())
    {
        printError("Filename '" + filename + "' does not exist");
        return;
    }

    while(!fHandle.eof())
    {
        string word;
        fHandle >> word;

        if(word.length() > 3)
        {
            word = stringToLower(word);
            addToMapADT<MapType>(map_type, word);
        }
    }
    fHandle.close();
}

void printAllMapStd(map<string, set<string>> &map_std)
{
    cout << "All anagrams: " << endl;
    for(auto it = map_std.begin(); it != map_std.end(); ++it)
    {
        if(it->second.size() > 1)
        {
            cout << it->first << " => [";
            for(auto vit = it->second.begin(); vit != it->second.end(); ++vit)
            {
                cout << *vit << ", ";
            }
            cout << "]" << endl;
        }
    }
    cout << endl;
}

template <typename MapType>
void printAllMapADT(MapType &map_type)
{
    cout << "All anagrams: " << endl;
    auto hash_it = map_type.getIterator();
    while(hash_it->hasNext())
    {
        auto element = hash_it->next();
        if(element->value.size() > 1)
        {
            cout << element->key << " => [";
            for(auto vit = element->value.begin(); vit != element->value.end(); ++vit)
            {
                cout << *vit << ", ";
            }
            cout << "]" << endl;
        }
    }
    delete hash_it;
    cout << endl;
}

#endif // SOLVE_H_
