#include "Geometry.hpp"
#include "Sphere.hpp"

using namespace rt;

int geometryCount = 7;

Geometry* scene[] = {
  new Sphere(-50, -25, 175, 30, 1, 0, 0),
  new Sphere(-10,   0, 100, 10, 1, 1, 0),
  new Sphere(  0,   0, 200, 40, 0, 1, 0),
  new Sphere(  0, -50, 200, 10, 1, 1, 1),
  new Sphere( 10,   0,  20,  5, 0, 1, 1),
  new Sphere(-70,   0, 100, 10, 1, 0, 1),
  new Sphere( 50,  25,  75, 50, 0, 0, 1)};
