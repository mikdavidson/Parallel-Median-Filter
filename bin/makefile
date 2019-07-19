JAVAC=/usr/bin/javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $<

default: JAVAC=/usr/bin/javac

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $<

classes: *.class
	$(CLASSES)

sourcefiles: *.java

clean:
	rm *.class
