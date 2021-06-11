SRCS = ./main/Expert

NAME = expert_system

all :		${NAME}

${NAME} :
			javac $(addsuffix .java, $(SRCS))

run :		${NAME}
			java ${SRCS} inputs/ex1 forward

rerun :		clean ${NAME}
			java ${SRCS} inputs/ex1 forward

clean :
			rm -f */*.class */*/*.class

re :		clean all