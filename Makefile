SRCS = Expert

NAME = expert_system

all :		${NAME}

${NAME} :
			javac $(addsuffix .java, $(SRCS))

run :		${NAME}
			java ${SRCS} inputs/example_input.txt

clean :
			rm -f *.class */*.class

re :		fclean all