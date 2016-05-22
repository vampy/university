#include "ingredient.hpp"
#include <string>
using namespace std;

Ingredient::Ingredient(unsigned int id, unsigned int quantity, string name, string producer)
{
    this->setId(id);
    this->setQuantity(quantity);
    this->setName(name);
    this->setProducer(producer);
}

unsigned int Ingredient::getId() const { return this->id; }

unsigned int Ingredient::getQuantity() const { return this->quantity; }

string Ingredient::getName() const { return this->name; }

string Ingredient::getProducer() const { return this->producer; }

void Ingredient::setId(unsigned int id) { this->id = id; }

void Ingredient::setQuantity(unsigned int quantity) { this->quantity = quantity; }

void Ingredient::setName(string name) { this->name = name; }

void Ingredient::setProducer(string producer) { this->producer = producer; }

void Ingredient::printToStdOut() const
{
    cout << setw(4) << this->id << setw(15) << this->quantity << setw(30) << this->name << setw(30) << this->producer
         << endl;
}
