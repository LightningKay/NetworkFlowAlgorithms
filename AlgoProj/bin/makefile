# java compiler
JC = javac

# set remove command according to OS
ifeq ($(OS), Windows_NT)
	CLEAN_COMMAND := del /s *.class
else
	CLEAN_COMMAND := find -name *.class -delete
endif

# directories in which java files exists
DIRECTORIES = . \
			algorithms \
			graph \
			graph/generation \
			model \
			run

# . java files
JAVA_FILES := $(foreach dir, $(DIRECTORIES), $(dir)/*.java)

# rule for converting .java into .class, $* is in-built macro that gets name of current target
.java.class:
	$(JC) $*.java
	

# default target
default: compile

compile: $(JAVA_FILES)
	$(JC) $(JAVA_FILES)

clean:
	$(CLEAN_COMMAND)
