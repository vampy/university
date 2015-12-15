include_directories(${CMAKE_SOURCE_DIR})
cmake_minimum_required(VERSION 2.8)

project(lab2-4)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c11")
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -Wall -g")

file(GLOB ALL_SRC "*.c")

add_executable(${PROJECT_NAME} ${ALL_SRC})
