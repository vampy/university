#include "Color.hpp"
#include "Vector.hpp"
#include "Line.hpp"
#include "Intersection.hpp"
#include "Material.hpp"

#ifndef RT_GEOMETRY_INCLUDED
#define RT_GEOMETRY_INCLUDED

namespace rt
{

class Geometry
{
private:
    Color m_color;
    Material m_material;

public:
    Geometry(const Color& color) : m_color(Color(color)) { }
    Geometry(const Geometry& geometry) { m_color = Color(geometry.getColor()); }

    virtual ~Geometry(){};

    virtual void setMaterial(const Material& material)
    {
        m_material = Material(material);
    }

    virtual const Material& getMaterial() const
    {
        return m_material;
    }

    virtual Intersection getIntersection(const Line& line, double minDist, double maxDist)
    {
        return Intersection(false, *this, line, 0);
    };

    Color getColor() const { return m_color; }
};
}

#endif
