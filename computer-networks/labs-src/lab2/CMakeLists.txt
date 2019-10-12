project(lab2)
include_directories(${CMAKE_SOURCE_DIR})
cmake_minimum_required(VERSION 2.8)

set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Wall")
set(SERVER_NAME server)
set(CLIENT_NAME client)
add_executable(
    ${SERVER_NAME}
    ${SERVER_NAME}.cpp
    ../os.hpp
    ../udp.hpp
)

add_executable(
    ${CLIENT_NAME}
    ${CLIENT_NAME}.cpp
    ../os.hpp
    ../udp.hpp
)

ADD_DEFINITIONS(
    -std=c++11
)
