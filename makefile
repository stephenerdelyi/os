JCOMPILER = javac
JRUNNER = java
MAIN = sim1
CONFIG = config_1.conf

default: compiler.class

compiler.class: $(MAIN).java
	@($(JCOMPILER) $(MAIN).java)
	clear
	@echo "✓ OS Compiled - Executable Now Available"

run: compiler.class
	@echo "✓ OS Executed - Operating System will Initialize"
	@($(JRUNNER) $(MAIN) $(CONFIG))

clean:
	rm -f *.class
	rm -f *.lgf
	clear
	@echo "✓ OS Cleaned - Executable No Longer Available"
