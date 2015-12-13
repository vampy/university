project(src)
include_directories(${CMAKE_SOURCE_DIR})
#link_directories(.)
cmake_minimum_required(VERSION 2.8)
#aux_source_directory(. SRC_LIST)
add_executable(
    ${PROJECT_NAME} 
    main.cpp 
    tests.cpp 
    util.hpp
    vector.hpp 
    list.hpp 
    ingredient.cpp 
    repository_filters.hpp 
    mem_repository.cpp 
    file_repository.cpp 
    repository.hpp 
    controller.cpp 
    ui.cpp
)

ADD_DEFINITIONS(
    -std=c++11
)
