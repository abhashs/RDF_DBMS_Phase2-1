#This Makefile is used to create assignments.

#the following macros should be updated according to
#the assignment to be generated

JARFILES=bufmgr/*.class diskmgr/*.class global/*.class iterator/*.class\
         heap/*.class chainexception/*.class  btree/*.class index/*.class tests/*.class\
		 labelheap/*.class quadrupleheap/*.class

# JDKPATH = /usr/java/jdk1.3.1_02
# JDKPATH = /usr/lib/jvm/java-8-openjdk-amd64
# LIBPATH = .:..
# LIBPATH = /home/abhash/ASU/Year4-Spring/CSE510/Project/src
JDKPATH = /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.322.b06-1.el7_9.x86_64
LIBPATH = /afs/asu.edu/users/a/k/s/akshrest/CSE510/minjava/javaminibase/src

CLASSPATH = $(LIBPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac -classpath $(CLASSPATH)
JAVA  = $(JDKPATH)/bin/java  -classpath $(CLASSPATH)

DOCFILES=bufmgr diskmgr global chainexception heap btree iterator index labelheap quadrupleheap

##############  update the above for each assignment in making

# ASSIGN=/home/candan/Courses/CSE510spr02/minjava/javaminibase/src
# ASSIGN=/home/abhash/ASU/Year4-Spring/CSE510/Project/src
ASSIGN=/afs/asu.edu/users/a/k/s/akshrest/CSE510/minjava/javaminibase/src
LIBDIR=$(ASSIGN)/lib
KEY=$(ASSIGN)/key
SRC=$(ASSIGN)/src

IMAGELINK=$(PACKAGEINDEX)/images
PACKAGEINDEX=$(ASSIGN)/javadoc

JAVADOC=javadoc -public -d $(PACKAGEINDEX)

### Generate jar and javadoc files.  Apply to most assignments.
db: 
	make -C global
	make -C chainexception
	make -C btree
	make -C bufmgr
	make -C diskmgr
	make -C heap
	make -C index
	make -C iterator
	make -C labelheap
	make -C quadrupleheap
	
doc:
	$(JAVADOC) $(DOCFILES)

# cd tests; make bmtest dbtest; whoami; make hftest bttest indextest jointest sorttest sortmerge qftest
test: 
	cd tests; make bmtest dbtest; whoami; make qftest

clean:
	\rm -f $(CLASSPATH)/*.class *~ \#* core $(JARFILES) TRACE

