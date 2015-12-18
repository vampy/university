include_directories(${CMAKE_SOURCE_DIR})
cmake_minimum_required(VERSION 2.8)

project(lab5-8)
set(CMAKE_CXX_FLAGS "${CMAKE_C_FLAGS} -std=c++11")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -Wall -g")

file(GLOB ALL_SRC "*.cpp" "*.hpp")

add_executable(${PROJECT_NAME} ${ALL_SRC})
