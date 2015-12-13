# include_directories(${CMAKE_SOURCE_DIR})
# set(HEADERS file1.h file2.h)
# set(SOURCES file1.c file2.c)
# add_executable(testc ${SOURCES} ${HEADERS})

project(iteration-1)

add_executable(iteration-1 main.c utils.c ui.c holiday.c validate.c vector.c test.c)

install(TARGETS iteration-1 RUNTIME DESTINATION bin)
