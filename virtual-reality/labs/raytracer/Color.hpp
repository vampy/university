#ifndef RT_COLOR_INCLUDED
#define RT_COLOR_INCLUDED

namespace rt
{

class Color
{
private:
    double m_red = 0, m_green = 0, m_blue = 0;
    bool m_is_normalized = true;

public:
    Color(bool is_normalized = true)
        : m_is_normalized(is_normalized)
    {
    }
    Color(double r, double g, double b, bool is_normalized = true)
        : m_red(r)
        , m_green(g)
        , m_blue(b)
        , m_is_normalized(is_normalized)
    {
    }

    Color(const Color& other)
    {
        m_red = other.m_red;
        m_green = other.m_green;
        m_blue = other.m_blue;
        m_is_normalized = other.m_is_normalized;
    }

    bool isNormalized() const { return m_is_normalized; }

    double getRed() const { return m_red; }

    double getGreen() const { return m_green; }

    double getBlue() const { return m_blue; }

    Color operator+(const Color& other) const
    {
        return Color(m_red + other.m_red, m_green + other.m_green, m_blue + other.m_blue);
    }

    Color& operator+=(const Color& other)
    {
        m_red += other.m_red;
        m_green += other.m_green;
        m_blue += other.m_blue;
        return *this;
    }

    Color operator-(const Color& other) const
    {
        return Color(m_red - other.m_red, m_green - other.m_green, m_blue - other.m_blue);
    }

    Color& operator-=(const Color& other)
    {
        m_red -= other.m_red;
        m_green -= other.m_green;
        m_blue -= other.m_blue;
        return *this;
    }

    Color operator*(const Color& other) const
    {
        return Color(m_red * other.m_red, m_green * other.m_green, m_blue * other.m_blue);
    }

    Color& operator*=(const Color& other)
    {
        m_red *= other.m_red;
        m_green *= other.m_green;
        m_blue *= other.m_blue;
        return *this;
    }

    Color operator/(const Color& other) const
    {
        return Color(m_red / other.m_red, m_green / other.m_green, m_blue / other.m_blue);
    }

    Color& operator/=(const Color& other)
    {
        m_red /= other.m_red;
        m_green /= other.m_green;
        m_blue /= other.m_blue;
        return *this;
    }

    Color operator*(double k) const { return Color(m_red * k, m_green * k, m_blue * k); }

    Color& operator*=(double k)
    {
        m_red *= k;
        m_green *= k;
        m_blue *= k;
        return *this;
    }

    Color operator/(double k) const { return Color(m_red / k, m_green / k, m_blue / k); }

    Color& operator/=(double k)
    {
        m_red /= k;
        m_green /= k;
        m_blue /= k;
        return *this;
    }
};
}

#endif
