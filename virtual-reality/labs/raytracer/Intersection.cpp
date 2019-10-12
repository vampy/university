#include "Intersection.hpp"
#include "Geometry.hpp"
#include "Line.hpp"

using namespace rt;

Intersection::Intersection(bool is_intersection, const Geometry& geometry, const Line& line, double t)
    : m_is_intersection(is_intersection)
    , m_geometry(&geometry)
    , m_line(&line)
    , _t(t)
    , m_vector(line.createLine(t))
{
}

Intersection::Intersection() {}
