#include "Geometry.hpp"
#include "Sphere.hpp"

using namespace rt;

int geometryCount = 3;

Geometry* scene[] = {
  new Sphere(-50, -25, 175, 30, 1, 0, 0),
  new Sphere(  0,   0, 200, 40, 0, 1, 0),
  new Sphere( 50,  25,  75, 50, 0, 0, 1)};
