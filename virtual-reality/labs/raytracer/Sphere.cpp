#include "Sphere.hpp"
#include <cmath>
#include <iostream>

using namespace rt;

/** See: https://en.wikipedia.org/wiki/Ray_tracing_%28graphics%29#Example */
Intersection Sphere::getIntersection(const Line& line, double minDist, double maxDist)
{
    double A, B, C, delta, t1, t2;

    // distance from the origin of the line to the center
    Vector X0C = line.getOrigin() - m_center;

    A = line.getDirection() * line.getDirection();
    B = line.getDirection() * X0C * 2;
    C = X0C * X0C - m_radius * m_radius;

    // B^2 - 4AC
    delta = B * B - 4 * A * C;

    // no solution
    if (delta < 0)
    {
        return Intersection();
    }

    // (-B +- sqrt(delta)) / 2A
    t1 = (-B + sqrt(delta)) / (2 * A);
    t2 = (-B - sqrt(delta)) / (2 * A);

    double min = std::min(t1, t2);
    if (min < minDist || min > maxDist)
    {
        return Intersection();
    }

    return Intersection(true, *this, line, min);
}
