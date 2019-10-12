#include "Vector.hpp"
#include "Line.hpp"
#include "Color.hpp"
#include "Intersection.hpp"

#ifndef RT_MATERIAL_INCLUDED
#define RT_MATERIAL_INCLUDED

namespace rt
{

class Light
{
private:
    Color m_ambient;
    Color m_diffuse;
    Color m_specular;
    double m_intensity = 0;
    Vector m_position;

public:
    Light()
    {
    }

    Light(const Color& ambient, const Color& diffuse, const Color& specular, double intensity, const Vector& position) :
        m_ambient(Color(ambient)),
        m_diffuse(Color(diffuse)),
        m_specular(Color(specular)),
        m_intensity(intensity),
        m_position(Vector(position))
    {

    }

    const double getIntensity() const { return m_intensity; }
    const Color& getAmbient() const { return m_ambient; }
    const Color& getDiffuse() const { return m_diffuse; }
    const Color& getSpecular() const { return m_specular; }
    const Vector& getPosition() const { return m_position; }
};

class Material
{
private:
    Color m_ambient;
    Color m_diffuse;
    Color m_specular;
    double m_shininess;
    double m_opacity;
    double m_reflectivity;

public:
    Material()
    {
    }

    Material(Color& ambient, Color& diffuse, Color& specular, double shininess, double opacity, double reflectivity)
    {
        m_ambient = Color(ambient);
        m_diffuse = Color(diffuse);
        m_specular = Color(specular);

        m_shininess = shininess;
        m_opacity = opacity;
        m_reflectivity = reflectivity;
    }

    Material(const Material& mat)
    {
        m_ambient = Color(mat.m_ambient);
        m_diffuse = Color(mat.m_diffuse);
        m_specular = Color(mat.m_specular);

        m_shininess = mat.m_shininess;
        m_opacity = mat.m_opacity;
        m_reflectivity = mat.m_reflectivity;
    }

    const Color& getAmbient() const { return m_ambient; }
    const Color& getDiffuse() const { return m_diffuse; }
    const Color& getSpecular() const { return m_specular; }
    const double getShininess() const { return m_shininess; }
    const double getOpacity() const { return m_opacity; }
    const double getReflectivity() const { return m_reflectivity; }
};
}

#endif
