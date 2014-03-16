################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../WikiClassStudent.cpp \
../simpleClassTest.cpp \
../wikiClasses.cpp 

OBJS += \
./WikiClassStudent.o \
./simpleClassTest.o \
./wikiClasses.o 

CPP_DEPS += \
./WikiClassStudent.d \
./simpleClassTest.d \
./wikiClasses.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 std=c++11 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


