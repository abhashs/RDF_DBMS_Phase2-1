# JDKPATH = /s/java
# LIBPATH = /p/course/cs784-raghu/minibase_share/javaminibase/construction
# JDKPATH = /usr/lib/jvm/java-8-openjdk-amd64
# LIBPATH = /home/abhash/ASU/Year4-Spring/CSE510/Project/src

JDKPATH = /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.322.b06-1.el7_9.x86_64
LIBPATH = /afs/asu.edu/users/a/k/s/akshrest/CSE510/minjava/javaminibase/src
CLASSPATH = $(JDKPATH)/lib/classes.zip:.:..:$(LIBPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac -classpath $(CLASSPATH)
JAVA  = $(JDKPATH)/bin/java  -classpath $(CLASSPATH)

PROGS = together

all: $(PROGS)

together:*.java
	$(JAVAC) *.java

clean:
	\rm -f *.class *~ \#* core 
