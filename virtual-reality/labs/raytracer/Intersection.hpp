#include <iostream>
#include "Vector.hpp"

#ifndef RT_INTERSECTION_INCLUDED
#define RT_INTERSECTION_INCLUDED

namespace rt
{

class Geometry;
class Line;

class Intersection
{
private:
    bool m_is_intersection = false;
    const Geometry* m_geometry = nullptr;
    const Line* m_line = nullptr;

    // actual distance
    double _t = 0;
    Vector m_vector; // the closest intersection vector
public:
    Intersection();
    Intersection(bool b, const Geometry& geometry, const Line& line, double t);

    double t() const { return _t; }

    bool isIntersection() const { return m_is_intersection; }
    const Vector& getVector() const { return m_vector; }
    const Geometry* getGeometry() const { return m_geometry; }
    const Line& getLine() const { return *m_line; }
};
}

#endif
