///////////////////
// Console: contains important output functions to the monitor or log file; also contains the error and crash functions
///////////////////
public class Console extends OS {
    WriteFile fileWriter = new WriteFile();

    //log - contains all important logic for logging to the monitor, log file, or both
    public void log(String echoStatement, boolean isFatal) {
        if (config != null) {
            if (config.logOption != null) {
                if (config.logOption.equals("Log to File")) {
                    writeFileLog(echoStatement, isFatal);
                } else if (config.logOption.equals("Log to Monitor")) {
                    writeConsoleLog(echoStatement, isFatal);
                } else if (config.logOption.equals("Log to Both")) {
                    writeConsoleLog(echoStatement, isFatal);
                    writeFileLog(echoStatement, isFatal);
                }
            } else {
                //since config file name hasn't loaded yet, just output to console
                writeConsoleLog(echoStatement, isFatal);
            }
        } else {
            //since config loaded yet, just output to console
            writeConsoleLog(echoStatement, isFatal);
        }

        //after we write everything, if the log was fatal we should ask to crash
        if (isFatal) {
            //if we allow fatal executions
            if (allowFatalExecution) {
                //prompt the user to continue
                console.writeConsoleLog("Continue execution? (Y/N)", false);
                String name = System.console().readLine();
                //if the user does not want to continue, then crash
                if (!name.equals("y") && !name.equals("Y")) {
                    crash();
                }
            } else {
                crash();
            }
        }
    }

    //writeConsoleLog - writes a log message to the monitor only
    public void writeConsoleLog(String echoStatement, boolean isFatal) {
        if (!isFatal) {
            System.out.println((char)27 + "[39m" + echoStatement);
        } else {
            System.out.println((char)27 + "[39m");
            System.out.println((char)27 + "[31m" + "✖ FATAL ERROR: " + echoStatement);
            System.out.println((char)27 + "[39m");
        }
    }

    //writeFileLog - writes a log message to the log file only
    public void writeFileLog(String echoStatement, boolean isFatal) {
        if (!isFatal) {
            fileWriter.write(config.logFileName, true, echoStatement);
        } else {
            fileWriter.write(config.logFileName, true, "✖ FATAL ERROR: " + echoStatement);
        }
    }

    //log - parameter override for single int printout
    public void log(int echoStatement) {
        log(Integer.toString(echoStatement), false);
    }

    //log - parameter override for cleaner log statement outside class
    public void log(String echoStatement) {
        log(echoStatement, false);
    }

    //error - writes a log message with isFatal set to true - will crash program
    public void error(String echoStatement) {
        log(echoStatement, true);
    }

    //printDiv - prints a divider to the monitor or log file
    public void printDiv() {
        log("=======================================================", false);
    }

    //printNewline - prints a new line to the monitor or log file
    public void printNewline() {
        log(" ", false);
    }

    //crash - crashes the program prematurely so that the files can not be processed after a syntax error is found
    private void crash() {
        System.exit(0);
    }
}
