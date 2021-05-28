SRCS = Expert

NAME = expert_system

all :		${NAME}

${NAME} :
			javac $(addsuffix .java, $(SRCS))

run :		${NAME}
			java ${SRCS} inputs/example_input.py

clean :
			rm -f *.class */*.class

re :		clean all