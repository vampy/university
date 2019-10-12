#include <cmath>
#include <ostream>

#ifndef RT_VECTOR_INCLUDED
#define RT_VECTOR_INCLUDED

using namespace std;

namespace rt
{

class Vector
{
private:
    double m_x = 0, m_y = 0, m_z = 0;

public:
    Vector() {}

    Vector(double x, double y, double z)
    {
        m_x = x;
        m_y = y;
        m_z = z;
    }

    Vector(const Vector& v)
    {
        m_x = v.m_x;
        m_y = v.m_y;
        m_z = v.m_z;
    }

    double getX() const { return m_x; }

    double getY() const { return m_y; }

    double getZ() const { return m_z; }

    Vector operator+(const Vector& other) const { return Vector(m_x + other.m_x, m_y + other.m_y, m_z + other.m_z); }

    Vector& operator+=(const Vector& other)
    {
        m_x += other.m_x;
        m_y += other.m_y;
        m_z += other.m_z;
        return *this;
    }

    Vector operator-(const Vector& other) const { return Vector(m_x - other.m_x, m_y - other.m_y, m_z - other.m_z); }

    Vector& operator-=(const Vector& other)
    {
        m_x -= other.m_x;
        m_y -= other.m_y;
        m_z -= other.m_z;
        return *this;
    }

    /** Dot product */
    double operator*(const Vector& other) const { return m_x * other.m_x + m_y * other.m_y + m_z * other.m_z; }

    /** Cross product */
    Vector operator^(const Vector& other) const
    {
        return Vector(
            m_y * other.m_z - m_z * other.m_y, m_z * other.m_x - m_x * other.m_z, m_x * other.m_y - m_y * other.m_x);
    }

    Vector operator*(double k) const { return Vector(m_x * k, m_y * k, m_z * k); }

    Vector& operator*=(double k)
    {
        m_x *= k;
        m_y *= k;
        m_z *= k;
        return *this;
    }

    Vector operator/(double k) const { return Vector(m_x / k, m_y / k, m_z / k); }

    Vector& operator/=(double k)
    {
        m_x /= k;
        m_y /= k;
        m_z /= k;
        return *this;
    }

    double length() const { return sqrt(m_x * m_x + m_y * m_y + m_z * m_z); }

    double length2() const { return m_x * m_x + m_y * m_y + m_z * m_z; }

    double normalize()
    {
        double norm = length();
        if (norm > 0.0)
        {
            double inv = 1.0 / norm;
            m_x *= inv;
            m_y *= inv;
            m_z *= inv;
        }
        return norm;
    }
};
} // namespace rt

#endif
