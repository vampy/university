#ifndef UTIL_H_
#define UTIL_H_
#include <algorithm>
#include <cctype>
#include <cstdlib>
#include <functional>
#include <iostream>
#include <locale>
#include <stdexcept>
#include <string>

#define UTIL_DEBUG true

// trim from start
static inline std::string& ltrim(std::string& s)
{
    s.erase(s.begin(), std::find_if(s.begin(), s.end(), std::not1(std::ptr_fun<int, int>(std::isspace))));
    return s;
}

// trim from end
static inline std::string& rtrim(std::string& s)
{
    s.erase(std::find_if(s.rbegin(), s.rend(), std::not1(std::ptr_fun<int, int>(std::isspace))).base(), s.end());
    return s;
}

// trim from both ends
static inline std::string& trim(std::string& s) { return ltrim(rtrim(s)); }

/**
 * Print to the screen in debug mode
 */
template <typename TElement>
void printDebug(TElement output)
{
    if (UTIL_DEBUG)
    {
        std::cout << "\033[0;36mDEBUG: " << output << "\033[0m" << std::endl;
    }
}

template <typename TElement>
void printError(TElement output)
{
    std::cout << "\033[0;31mERROR: " << output << "\033[0m" << std::endl;
}

template <typename TElement>
void printWarning(TElement output)
{
    std::cout << "\033[0;33mWARNING: " << output << "\033[0m" << std::endl;
}

template <typename TElement>
void printSuccess(TElement output)
{
    std::cout << "\033[0;32m" << output << "\033[0m" << std::endl;
}

/**
 * Read an int into str_int
 */
inline bool stringToInt(std::string str, int& str_int)
{
    try
    {
        str_int = std::stoi(trim(str));
    }
    catch (std::invalid_argument& e)
    {
        return false;
    }

    return true;
}

inline bool stringToInt(std::string str, unsigned int& str_int)
{
    try
    {
        str_int = std::stoi(trim(str));
    }
    catch (std::invalid_argument& e)
    {
        return false;
    }

    return true;
}
#endif // UTIL_H_
