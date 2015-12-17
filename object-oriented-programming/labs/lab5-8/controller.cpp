#include <string>
#include "controller.hpp"
#include "repository.hpp"

Controller::Controller(Repository *repository)
{
    this->repository = repository;
}

bool Controller::addIngredient(unsigned int id, unsigned int quantity, string name, string producer)
{
    if(this->repository->exists(id))
    {
        return false;
    }

    this->repository->addIngredient(new Ingredient(id, quantity, name, producer));

    return true;
}

bool Controller::removeIngredient(unsigned int id)
{
    if(!this->repository->exists(id))
    {
        return false;
    }
    this->repository->removeIngredient(id);

    return true;
}

bool Controller::changeName(unsigned int id, string name)
{
    if(!this->repository->exists(id))
    {
        return false;
    }

    this->repository->getById(id)->setName(name);

    return true;
}

bool Controller::changeProducer(unsigned int id, string producer)
{
    if(!this->repository->exists(id))
    {
        return false;
    }

    this->repository->getById(id)->setProducer(producer);

    return true;
}

bool Controller::changeQuantity(unsigned int id, unsigned int quantity)
{
    if(!this->repository->exists(id))
    {
        return false;
    }

    this->repository->getById(id)->setQuantity(quantity);

    return true;
}

void Controller::filterByQuantity(unsigned int quantity, int filter_type = 0)
{
    switch (filter_type) {
    case -1:
    {
        printDebug("Filter quantity less than");
        auto filter = new FilterQuantityLess(quantity);
        this->repository->filterBy(*filter);
        delete filter;
        break;
    }
    case 0:
    {
        printDebug("Filter quantity equal to");
        auto filter = new FilterQuantityEqual(quantity);
        this->repository->filterBy(*filter);
        delete filter;
        break;
    }
    case 1:
    {
        printDebug("Filter quantity greater than");
        auto filter = new FilterQuantityGreater(quantity);
        this->repository->filterBy(*filter);
        delete filter;
        break;
    }
    default:
        throw ("filter_type not valid");
        break;
    }
}

void Controller::filterByName(string name)
{
    auto filter = new FilterNameEqual(name);
    this->repository->filterBy(*filter);
    delete filter;
}

void Controller::sortByProducer(bool reverse = false)
{
    if(reverse) // descending
    {
        this->repository->sortBy([](const Ingredient *a, const Ingredient *b)
        {
            if(a->getProducer() == b->getProducer()) // second criteria
            {
                return a->getQuantity() > b->getQuantity();
            }

            return a->getProducer() > b->getProducer();
        });
    }
    else // asscending
    {
        this->repository->sortBy([](const Ingredient *a, const Ingredient *b)
        {
            if(a->getProducer() == b->getProducer()) // second criteria
            {
                return a->getQuantity() < b->getQuantity();
            }

            return a->getProducer() < b->getProducer();
        });
    }
}

void Controller::sortByName(bool reverse = false)
{
    if(reverse) // descending
    {
        this->repository->sortBy([](const Ingredient *a, const Ingredient *b)
        {
            if(a->getName() == b->getName()) // second criteria
            {
                return a->getQuantity() > b->getQuantity();
            }

            return a->getName() > b->getName();
        });
    }
    else // asscending
    {
        this->repository->sortBy([](const Ingredient *a, const Ingredient *b)
        {
            if(a->getName() == b->getName()) // second criteria
            {
                return a->getQuantity() < b->getQuantity();
            }

            return a->getName() < b->getName();
        });
    }
}

void Controller::sortByQuantity(bool reverse)
{
    if(reverse) // descending
    {
        this->repository->sortBy([](const Ingredient *a, const Ingredient *b)
        {
            if(a->getQuantity() == b->getQuantity()) // second criteria
            {
                return a->getId() > b->getId();
            }

            return a->getQuantity() > b->getQuantity();
        });
    }
    else // asscending
    {
        this->repository->sortBy([](const Ingredient *a, const Ingredient *b)
        {
            if(a->getQuantity() == b->getQuantity()) // second criteria
            {
                return a->getId() < b->getId();
            }

            return a->getQuantity() < b->getQuantity();
        });
    }
}

bool Controller::undo()
{
    return this->repository->undo();
}

Repository *Controller::getRepository()
{
    return this->repository;
}
