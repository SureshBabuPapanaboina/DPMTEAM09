CC = g++
CFLAGS = -c -Wall -std=c++0x

all: simpleClassTest

gradingClassTest: gradingClassTest.o wikiClassesStudent.o wikiClasses.o
	$(CC) gradingClassTest.o wikiClassesStudent.o wikiClasses.o -o gradingClassTest

simpleClassTest: simpleClassTest.o wikiClassesStudent.o wikiClasses.o
	$(CC) simpleClassTest.o wikiClassesStudent.o wikiClasses.o -o simpleClassTest


simpleClassTest.o: simpleClassTest.cpp wikiClasses.h
	$(CC) $(CFLAGS) simpleClassTest.cpp wikiClasses.cpp

gradingClassTest.o: gradingClassTest.cpp wikiClasses.h
	$(CC) $(CFLAGS) gradingClassTest.cpp


wikiClassesStudent.o: wikiClassesStudent.cpp wikiClasses.h
	$(CC) $(CFLAGS) wikiClassesStudent.cpp

wikiClasses.o: wikiClasses.cpp wikiClasses.cpp
	$(CC) $(CFLAGS) wikiClasses.cpp

clean: 
	rm -rf *.o simpleClassTest gradingClassTest
