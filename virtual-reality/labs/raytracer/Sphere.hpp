#include "Vector.hpp"
#include "Line.hpp"
#include "Intersection.hpp"
#include "Geometry.hpp"
#include "Color.hpp"

#ifndef RT_SPHERE_INCLUDED
#define RT_SPHERE_INCLUDED

namespace rt
{

class Sphere : public Geometry
{
private:
    /** The sphere center location */
    Vector m_center;

    /** Radius of the sphere */
    double m_radius;

public:
    Sphere(const Vector& center, double radius, const Color& color)
        : Geometry(color)
    {
        m_center = Vector(center);
        m_radius = radius;
    }

    Sphere(double x, double y, double z, double radius, double r, double g, double b)
        : Geometry(Color(r, g, b))
    {
        m_center = Vector(x, y, z);
        m_radius = radius;
    }

    virtual Intersection getIntersection(const Line& line, double minDist, double maxDist);

    double getRadius() const { return m_radius; }
    Vector getCenter() const { return m_center; }
};
}

#endif
