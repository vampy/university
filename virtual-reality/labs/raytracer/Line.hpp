#include "Vector.hpp"

#ifndef RT_LINE_INCLUDED
#define RT_LINE_INCLUDED

namespace rt
{

class Line
{
private:
    /* origin of the line */
    Vector m_x0;

    /* direction of line */
    Vector m_dx;

public:
    Line(const Vector& x0, const Vector& x1, bool parametric = false)
    {
        m_x0 = Vector(x0);

        // If the line is parametric then it means we do not need to calculate the direction ourselfs
        if (parametric)
            m_dx = Vector(x1);
        else
            m_dx = Vector(x1 - x0);

        // just in case, do not trust the parametric parameter
        m_dx.normalize();
    }

    Line(const Line& line)
    {
        m_x0 = Vector(line.m_x0);
        m_dx = Vector(line.m_dx);
    }

    const Vector& getOrigin() const { return m_x0; }
    const Vector& getDirection() const { return m_dx; }

    /** Construct a new line with the origin of this line and direction but different length, t = length */
    Vector createLine(double t) const { return Vector(m_x0 + m_dx * t); }
};
}

#endif
