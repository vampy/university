project(sda)
include_directories(${CMAKE_SOURCE_DIR})
#link_directories(.)
cmake_minimum_required(VERSION 2.8)
#aux_source_directory(. SRC_LIST)
add_executable(
    ${PROJECT_NAME} 
    main.cpp 
    vector.hpp
    util.hpp
    map_adt.hpp
    map_vector.hpp
    map_hash.hpp
    solve.hpp
    tests/test_map_hash.hpp
    tests/test_map_vector.hpp
    tests/test_vector.hpp
)

ADD_DEFINITIONS(
    -std=c++11
    -Wall
    -g
)
