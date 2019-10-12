project(lab5)
include_directories(${CMAKE_SOURCE_DIR})
SET(CURSES_NEED_NCURSES TRUE)
cmake_minimum_required(VERSION 2.8)
find_package(Threads REQUIRED)
find_package(Curses REQUIRED)

set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Wall")
set(PNAME chat)
include_directories(${CURSES_INCLUDE_DIR})
add_executable(
    ${PNAME}
    ${PNAME}.cpp
    ../os.hpp
    ../socket.hpp
    ../tcp.hpp
    ../udp.hpp
)
target_link_libraries(${PNAME} ${CMAKE_THREAD_LIBS_INIT} ${CURSES_LIBRARIES})

ADD_DEFINITIONS(
    -std=c++11
)

