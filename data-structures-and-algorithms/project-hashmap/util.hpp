#ifndef UTIL_H_
#define UTIL_H_
#include <iostream>
#include <string>
#include <cstdlib>
#include <stdexcept>
#include <algorithm>
#include <functional>
#include <cctype>
#include <locale>

#define UTIL_DEBUG false

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
template <typename TElement>
inline bool stringToInt(std::string str, TElement& str_int)
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

/**
 * Convert a string to lower case
 * @return string
 */
inline std::string stringToLower(std::string str)
{
    std::transform(str.begin(), str.end(), str.begin(), ::tolower);
    return str;
}

/**
 * Convert a string to upper case
 * @return string
 */
inline std::string stringToUpper(std::string str)
{
    std::transform(str.begin(), str.end(), str.begin(), ::toupper);
    return str;
}

#endif // UTIL_H_
