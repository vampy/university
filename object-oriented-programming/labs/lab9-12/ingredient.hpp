#ifndef INGREDIENT_H_
#define INGREDIENT_H_
#include <iomanip>
#include <iostream>
#include <string>

using namespace std;

class Ingredient
{
public:
    Ingredient() {}

    Ingredient(unsigned int, unsigned int, string, string);

    Ingredient& operator=(const Ingredient&);

    ~Ingredient() {}

    /**
     * Get the id
     *
     * return unsigned int
     */
    unsigned int getId() const;

    /**
     * Get the quantity
     *
     * return unsigned int
     */
    unsigned int getQuantity() const;

    /**
     * Get the producer
     *
     * return string
     */
    string getProducer() const;

    string getName() const;

    void setId(unsigned int);

    void setQuantity(unsigned int);

    void setProducer(string);

    void setName(string);

    void printToStdOut() const;

protected:
    unsigned int id;
    unsigned int quantity;
    string name;
    string producer;
};

#endif // INGREDIENT_H_
