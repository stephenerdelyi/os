JFLAGS = -g
JRUNNER = java
MAIN = OS
CONFIG = config_1.conf
OSNUM = 4
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
		OSMutex.java\
		Locks.java\
		MemoryBlock.java\
		Scheduler.java

default: classes

classes: $(CLASSES:.java=.class)
		clear
		@echo "✓ OS Compiled - Runtime Dependencies Now Available [$(MAIN)]"
		@echo "✓ OS Executed - Operating System will Initialize"
		@($(JC) $(MAIN).java)
		@($(JRUNNER) $(MAIN) $(CONFIG))

norun: $(CLASSES:.java=.class)
		clear
		@echo "✓ OS Compiled - Runtime Dependencies Now Available [$(MAIN)]"
		@echo ""

clean:
		$(RM) *.class
		$(RM) *.lgf
		$(RM) *.zip
		clear
		@echo "✓ OS Cleaned - Runtime Dependencies No Longer Available"
		@echo ""

export:
		@(zip -r "SimO$(OSNUM)_ErdelyiStephen.zip" . -x "*.mdf" "*.conf" ".git/*" "*.class" "*.lgf" "*.zip")
		clear
		@echo "✓ OS Exported - ZIP File Now Available [SimO$(OSNUM)]"
		@echo ""

commands:
		clear
		@echo "///////////////////////////////////////////////////////////////"
		@echo "//                    MAKEFILE COMMANDS                      //"
		@echo "///////////////////////////////////////////////////////////////"
		@echo "make      	makes the executable and runs the program"
		@echo "make norun	makes the executable w/o running the program"
		@echo "make clean	removes the executable and unnecessary files"
		@echo "make export	makes a packaged .zip for submission"
		@echo ""
