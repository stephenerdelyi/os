JCOMPILER = javac
JRUNNER = java
MAIN = sim2
CONFIG = config_1.conf
#INPSTRING = $(MAKECMDGOALS)
#CUSTOMCONFIG = ${INPSTRING[1]}

default:
	@echo ""
	@echo "Makefile Options:"
	@echo ""
	@echo "make compile - compiles the java code"
	@echo "make run     - compiles and runs the java code with config_1.conf"
	@echo "make clean   - removes all .class and .lgf files"
	@echo ""

compile: $(MAIN).java
	@($(JCOMPILER) $(MAIN).java)
	clear
	@echo "✓ OS Compiled - Executable Now Available [$(MAIN)]"

run: compile
	@echo "✓ OS Executed - Operating System will Initialize"
	@($(JRUNNER) $(MAIN) $(CONFIG))

#custom: compile
#	@echo "✓ OS Executed - Operating System will Initialize"
#	@($(JRUNNER) $(MAIN) $(2))

clean:
	rm -f *.class
	rm -f *.lgf
	clear
	@echo "✓ OS Cleaned - Executable No Longer Available"
