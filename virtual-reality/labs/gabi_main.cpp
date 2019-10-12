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
#include "Light.hpp"

#include "Scene.hpp"

using namespace std;
using namespace rt;

double imageToViewPlane(int n, int imgSize, double viewPlaneSize) {
    double u = (double)n*viewPlaneSize/(double)imgSize;
    u -= viewPlaneSize/2;
    return u;
}

const Intersection findFirstIntersection(const Line& ray,
                                         double minDist, double maxDist) {
    Intersection intersection;

    for(int i=0; i<geometryCount; i++) {
        Intersection in = scene[i]->getIntersection(ray, minDist, maxDist);
        if(in.valid()) {
            if(!intersection.valid()) {
                intersection = in;
            }
            else if(in.t() < intersection.t()) {
                intersection = in;
            }
        }
    }

    return intersection;
}

int main() {
    Vector viewPoint(0, 0, 0);
    Vector viewDirection(0, 0, 1);
    Vector viewUp(0, -1, 0);

    double frontPlaneDist = 2;
    double backPlaneDist = 1000;

    double viewPlaneDist = 100;
    double viewPlaneWidth = 200;
    double viewPlaneHeight = 100;

    int imageWidth = 1000;
    int imageHeight = 500;

    Vector viewParallel = viewUp^viewDirection;

    viewDirection.normalize();
    viewUp.normalize();
    viewParallel.normalize();

    Vector x1;
    Vector lightPosition(-333, 100,-100);

    Color lightAmbientColor(1, 1, 1);
    Color lightDiffuseColor(1, 1, 1);
    Color lightSpecularColor(1, 1, 1);
    Light light(lightAmbientColor, lightDiffuseColor, lightSpecularColor, 0.8, lightPosition);


    Color c(72,72,43);
    Image image(imageWidth, imageHeight);
    int i, j;
    for(i = 0;i<imageWidth;i++)
        for(j = 0;j<imageHeight;j++){
            x1 = viewPoint + viewDirection * viewPlaneDist + viewUp * imageToViewPlane(j,imageHeight,viewPlaneHeight) +
                    viewParallel*imageToViewPlane(i,imageWidth,viewPlaneWidth);

            Line ray(viewPoint,x1,false);
            Intersection in = findFirstIntersection(ray,frontPlaneDist,backPlaneDist);
            if(in.valid()){
                Color matAmbientColor = in.geometry().color() * 0.1;
                Color matDiffuseColor = in.geometry().color() * 0.3;
                Color matSpecularColor = in.geometry().color()* 0.7;
                Material mat(matAmbientColor,matDiffuseColor,matSpecularColor,100,0,0);
                Color c = mat.ambient() * light.ambient();
                
                Vector V=ray.vec(in.t());
                Sphere sphere =(Sphere&) in.geometry();
                
                Vector N = V-sphere.center();
                N.normalize();
                
                Vector T = light.position() - V;
                T.normalize();
                
                if(N*T>0){
                    c+=mat.diffuse() * light.diffuse() * (N*T);
                }
                
                
                Vector E=viewPoint-V;
                E.normalize();
                
                Vector R = N*(N*T)*2 - T;
                R.normalize();
                
                if(E*R>0){
                    c+=mat.specular()*light.specular()* pow(E*R,mat.shininess());
                }
                image.setPixel(i,j,c);
                //image.setPixel(i,j,in.geometry().color());
                //          else
                //          image.setPixel(i,j,c);
            }
        }
    // ADD CODE HERE

    image.store("scene.ppm");

    for(int i=0; i<geometryCount; i++) {
        delete scene[i];
    }

    return 0;
}

