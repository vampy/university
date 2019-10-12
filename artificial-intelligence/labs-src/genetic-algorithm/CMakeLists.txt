project(ga)
include_directories(${CMAKE_SOURCE_DIR})
cmake_minimum_required(VERSION 2.8)

set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Wall")
set(COMPILE_NAME ga)
add_executable(
    ${COMPILE_NAME}
    ${COMPILE_NAME}.cpp
)