#include <cmath>
#include <iostream>
#include <string>

#include "Vector.hpp"
#include "Line.hpp"
#include "Geometry.hpp"
#include "Sphere.hpp"
#include "Image.hpp"
#include "Color.hpp"
#include "Intersection.hpp"
#include "Material.hpp"

#include "Scene.hpp"

using namespace std;
using namespace rt;

double imageToViewPlane(int n, uint16_t imgSize, double viewPlaneSize)
{
    double u = (double) n * viewPlaneSize / (double) imgSize;
    u -= viewPlaneSize / 2;
    return u;
}

const Intersection findFirstIntersection(const Line& ray, double minDist, double maxDist)
{
    Intersection closestIntersection;

    for (size_t i = 0; i < geometryCount; i++)
    {
        Intersection in = scene[i]->getIntersection(ray, minDist, maxDist);
        if (in.isIntersection())
        {
            // closest intersection is not a valid intersection OR the distance is greater that the new found
            // intersection
            if (!closestIntersection.isIntersection() || closestIntersection.t() > in.t())
            {
                closestIntersection = in;
            }
        }
    }

    return closestIntersection;
}

int main()
{
    Vector viewPoint(0, 0, 0);
    Vector viewDirection(0, 0, 1);
    Vector viewUp(0, -1, 0);
    Vector viewParallel = viewUp ^ viewDirection;

    // Used for finding the intersction, if objects too close or too far away, do not display them
    double frontPlaneDist = 2, backPlaneDist = 1000;

    // Define viewport
    double viewPlaneDist = 100, viewPlaneWidth = 200, viewPlaneHeight = 100;

    // The size of the output image
    uint16_t imageWidth = 1000, imageHeight = 500;

    // Light
    Color ambient(1, 1, 1), diffuse(1, 1, 1), specular(1, 1, 1);
    Vector light_position(-100, 0, 20);
    Light light(ambient, diffuse, specular, 1, light_position);
    //Material material(ambient, diffuse, specular, 0.2, 0.8, 0.2);

    viewDirection.normalize();
    viewUp.normalize();
    viewParallel.normalize();

    for (size_t i = 0; i < geometryCount; i++)
    {
        Color color = scene[i]->getColor(), a, b, c;
        a = color * 0.2;
        b = color * 0.4;
        c = color * 0.5;
        Material material(a, b, c, 2, 1, 1);
        scene[i]->setMaterial(material);
    }

    // Point a ray for every pixel on the screen, raytracer.
    Image image(imageWidth, imageHeight);
    for (uint16_t x = 0; x < imageWidth; x++)
    {
        for (uint16_t y = 0; y < imageHeight; y++)
        {
            Vector x1 = viewPoint + viewDirection * viewPlaneDist /** start from here */
                + viewUp * imageToViewPlane(y, imageHeight, viewPlaneHeight) /** calculate height */
                + viewParallel * imageToViewPlane(x, imageWidth, viewPlaneWidth) /** calculate width */;
            Line viewRay(viewPoint, x1);

            Intersection in = findFirstIntersection(viewRay, frontPlaneDist, backPlaneDist);
            if (in.isIntersection())
            {
                // See: https://en.wikipedia.org/wiki/Ray_tracing_%28graphics%29#Example
                // and http://www.cs.ubbcluj.ro/~rares/course/vr/res/rt/lighting.png
                Material material = in.getGeometry()->getMaterial();
                Color pixelColor = material.getAmbient() * light.getAmbient();

                // Where does it intersect the surface
                Vector intersectionPoint = viewRay.createLine(in.t());
                Vector normalSurface = intersectionPoint - static_cast<const Sphere*>(in.getGeometry())->getCenter();
                normalSurface.normalize();

                // Vector from the intersection point to the light
                Vector normalLight = light.getPosition() - intersectionPoint;
                normalLight.normalize();

                // Vector from the intersection point to the camera
                Vector normalCamera = viewPoint - intersectionPoint;
                normalCamera.normalize();

                // Normal Surface * Normal Light, dot product
                double dotNormalLight = normalSurface * normalLight;

                // Reflection vector of the surface
                Vector normalReflection =  normalSurface * dotNormalLight * 2 - normalLight;
                normalReflection.normalize();

                // Normal camera * Normal reflection, dot product
                double dotCameraReflection = normalCamera * normalReflection;

                // Choose right color
                if (dotNormalLight > 0)
                {
                    pixelColor += material.getDiffuse() * light.getDiffuse() * dotNormalLight;
                }
                if (dotCameraReflection > 0)
                {
                    pixelColor += material.getSpecular() * light.getSpecular() * pow(dotCameraReflection, material.getShininess());
                }
                pixelColor *= light.getIntensity();

                image.setPixel(x, y, pixelColor);
            }
        }
    }

    image.store("scene.ppm");

    // Cleanup
    for (size_t i = 0; i < geometryCount; i++)
    {
        delete scene[i];
    }

    return 0;
}
