makeExec()
{
	javac sim1.java
	echo "✓ OS Compiled - Executable Now Available"
}

run()
{
	echo "✓ OS Executed - Operating System will Initialize"
	java sim1 config_1.conf
}

makeRun()
{
	makeExec
	run
}

clean()
{
	rm -f *.class
	rm -f *.lgf
	clear
	echo "✓ OS Cleaned - Executable No Longer Available"
}

clear
if [ "$1" = "" ]
then
	makeExec
elif [ "$1" = "run" ]
then
	run
elif [ "$1" = "clean" ]
then
	clean
elif [ "$1" = "makeRun" ]
then
        makeRun
else
	echo "✖ Invalid input parameter - no make actions completed"
fi
