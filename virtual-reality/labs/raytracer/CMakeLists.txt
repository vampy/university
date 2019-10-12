project(raytracer)
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

include_directories(${CMAKE_SOURCE_DIR})
cmake_minimum_required(VERSION 2.8)

set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Wall -g")
set(COMPILE_NAME raytracer)

file(GLOB ALL_SRC
    "*.h"
    "*.hpp"
    "*.cpp"
)

add_executable(
    ${COMPILE_NAME}
    ${ALL_SRC}
)