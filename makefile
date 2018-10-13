JFLAGS = -g
JRUNNER = java
MAIN = OS
CONFIG = config_1.conf
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Console.java\
		WriteFile.java\
		ReadFile.java\
		StringHelper.java\
		ValidKeys.java\
		Task.java\
		ConfigFile.java\
		TaskStackQueue.java\
		FileHandler.java\
		PCB.java\
		Clock.java\
		TaskProcessor.java\
		OSThread.java\
		OSSemaphore.java\
		Semaphores.java

default: classes

classes: $(CLASSES:.java=.class)
		clear
		@echo "✓ OS Compiled - Executable Now Available [$(MAIN)]"
		@echo "✓ OS Executed - Operating System will Initialize"
		@($(JC) $(MAIN).java)
		@($(JRUNNER) $(MAIN) $(CONFIG))

norun: $(CLASSES:.java=.class)
		clear
		@echo "✓ OS Compiled - Executable Now Available [$(MAIN)]"
		@echo ""
clean:
		$(RM) *.class
		$(RM) *.lgf
		clear
		@echo "✓ OS Cleaned - Executable No Longer Available"
		@echo ""
